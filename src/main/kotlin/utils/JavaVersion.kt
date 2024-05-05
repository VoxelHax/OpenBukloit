package utils

import cli.Logs
import dev.virefire.yok.Yok
import org.rauschig.jarchivelib.ArchiverFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

fun getJavaVersion(major: Int, minor: Int): String {
    return when (major to minor) {
        45 to 0 -> "1.0"
        45 to 3 -> "1.1"
        46 to 0 -> "1.2"
        47 to 0 -> "1.3"
        48 to 0 -> "1.4"
        else -> (major - (49 - 5)).toString()
    }
}

fun getOsType(): String {
    val os = System.getProperty("os.name", "generic").lowercase()
    return if (os.contains("mac") || os.contains("darwin")) {
        "mac"
    } else if (os.contains("win")) {
        "windows"
    } else if (os.contains("nux")) {
        "linux"
    } else {
        os.split(" ")[0]
    }
}

fun requireJDK(major: Int, minor: Int): Path {
    if (major < 52) throw Exception("Class version $major.$minor is not supported by OpenBukloit")

    val versionId = getJavaVersion(major, minor)
    val jdkDir = File("./.openbukloit/jdk/$versionId")
    if (Paths.get(jdkDir.canonicalPath, "bin", "javac").toFile().exists())
        return Paths.get(jdkDir.canonicalPath, "bin", "javac")
    if (Paths.get(jdkDir.canonicalPath, "bin", "javac.exe").toFile().exists())
        return Paths.get(jdkDir.canonicalPath, "bin", "javac.exe")
    if (jdkDir.exists()) jdkDir.deleteRecursively()

    Logs.info("JDK $versionId not found, downloading (this may take some time)...")
    val currentOs = getOsType()
    var currentArchitecture = System.getProperty("os.arch").lowercase()
    if (currentArchitecture == "x86_64") currentArchitecture = "x64"
    if (currentArchitecture == "amd64") currentArchitecture = "x64"
    if (currentArchitecture == "i386") currentArchitecture = "x32"
    if (currentArchitecture == "arm64") currentArchitecture = "arm"

    var extension = ".tar.gz"
    if(currentOs == "windows")
        extension = ".zip"

    val binaryName = "jdk-$versionId-$currentOs-$currentArchitecture$extension"
    val url = "https://api.adoptium.net/v3/binary/latest/$versionId/ga/$currentOs/$currentArchitecture/jdk/hotspot/normal/eclipse?project=jdk"
    Logs.info("URL: $url")

    File("./.openbukloit/temp/jdk").mkdirs()
    val downloaded = Paths.get("./.openbukloit/temp/jdk", binaryName)
    Files.copy(Yok.get(url).body.stream, downloaded)

    Logs.info("JDK $versionId downloaded, extracting...")
    val archiver = if (binaryName.endsWith(".tar.gz"))
        ArchiverFactory.createArchiver("tar", "gz")
    else
        ArchiverFactory.createArchiver("zip")
    archiver.extract(downloaded.toFile(), jdkDir)

    val subFiles = jdkDir.listFiles()!!
    if (subFiles.size == 1) {
        copyDirectory(subFiles[0], jdkDir)
        subFiles[0].deleteRecursively()
    }

    Logs.info("JDK $versionId installed successfully")
    if (Paths.get(jdkDir.canonicalPath, "bin", "javac").toFile().exists())
        return Paths.get(jdkDir.canonicalPath, "bin", "javac")
    if (Paths.get(jdkDir.canonicalPath, "bin", "javac.exe").toFile().exists())
        return Paths.get(jdkDir.canonicalPath, "bin", "javac.exe")
    throw Exception("JDK $versionId not found")
}

fun toJRE(jdkPath: Path): Path {
    val dir = jdkPath.parent.toFile()
    if (Paths.get(dir.canonicalPath, "java").toFile().exists())
        return Paths.get(dir.canonicalPath, "java")
    if (Paths.get(dir.canonicalPath, "java.exe").toFile().exists())
        return Paths.get(dir.canonicalPath, "java.exe")
    throw Exception("JRE not found")
}
