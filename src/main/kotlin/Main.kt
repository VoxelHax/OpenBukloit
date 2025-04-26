import cli.boolArg
import cli.findArg
import injector.process
import cli.Logs
import utils.PreparedExploit
import utils.loadExploit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.file.Paths

import java.nio.file.Files

fun run(args: Array<String>) {
    val exploit = loadExploit(findArg("exploit", "e", args))
    val preparedExploit = PreparedExploit(exploit)
    val mode = findArg("mode", "m", args) ?: "multiple"
    if (mode !in listOf("multiple", "single")) throw Exception("Invalid mode: $mode")
    val input = findArg("input", "i", args) ?: if (mode == "multiple") "in" else "in.jar"
    val output = findArg("output", "o", args) ?: if (mode == "multiple") "out" else "out.jar"
    val replace = boolArg("replace", "r", args)
    val traceErrors = boolArg("trace-errors", "tr", args)

    val noCamouflage = boolArg("no-camouflage", null, args)
    val className = findArg("class-name", null, args)
    val methodName = findArg("method-name", null, args)

    val inputFiles = if (mode == "multiple") {
        val inputDir = Paths.get(input)
        if (!inputDir.exists()) throw Exception("Input directory does not exist: $input")
        if (!inputDir.isDirectory()) throw Exception("Input is not a directory: $input")
        inputDir.toFile().listFiles()?.filter { it.extension == "jar" }?.map { it.toPath() } ?: emptyList()
    } else {
        listOf(Paths.get(input))
    }

    val outputFiles = if (mode == "multiple") {
        val outputDir = Paths.get(output)
        if (!outputDir.exists()) Files.createDirectories(outputDir)
        if (!outputDir.isDirectory()) throw Exception("Output is not a directory: $output")
        inputFiles.map {
            outputDir.resolve(it.fileName)
        }
    } else {
        listOf(File(output))
    }

    val exploitParams = mutableMapOf<String, String>()

    for (exploitParam in preparedExploit.params) {
        exploitParams[exploitParam] = findArg(exploitParam, args = args, last = true)
            ?: throw Exception("Exploit parameter not found: $exploitParam")
    }

    for (i in inputFiles.indices) {
        try {
            val exploitClass = preparedExploit.make(exploitParams)
            process(
                inputFiles[i],
                outputFiles[i],
                exploitClass,
                replace,
                noCamouflage,
                className,
                methodName,
            )
        } catch (e: Exception) {
            handleError(e, traceErrors)
        }
    }
}

fun main(args: Array<String>) {
    Files.createDirectories(Paths.get("./.openbukloit/temp"))
    try {
        run(args)
    } catch (e: Exception) {
        handleError(e, true)
    }
    Paths.get("./.openbukloit/temp").toFile().deleteRecursively()

fun handleError(e: Exception, traceErrors: Boolean) {
    if (Logs.task) Logs.finish()
    Logs.error("${e::class.qualifiedName}: ${e.message}")
    if (traceErrors) {
        val buff = ByteArrayOutputStream()
        e.printStackTrace(PrintStream(buff))
        buff.toString().lines().filter { it.isNotBlank() }.forEach { Logs.error(it) }
    }
}
}
