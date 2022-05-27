import cli.boolArg
import cli.findArg
import injector.process
import cli.Logs
import utils.PreparedExploit
import utils.loadExploit
import java.io.File
import java.nio.file.Paths

fun run(args: Array<String>) {
    val exploit = loadExploit(findArg("exploit", "e", args))
    val preparedExploit = PreparedExploit(exploit)
    val mode = findArg("mode", "m", args) ?: "multiple"
    if (mode !in listOf("multiple", "single")) throw Exception("Invalid mode: $mode")
    val input = findArg("input", "i", args) ?: if (mode == "multiple") "in" else "in.jar"
    val output = findArg("output", "o", args) ?: if (mode == "multiple") "out" else "out.jar"
    val replace = boolArg("replace", "r", args)

    val inputFiles = if (mode == "multiple") {
        val inputDir = File(input)
        if (!inputDir.exists()) throw Exception("Input directory does not exist: $input")
        if (!inputDir.isDirectory) throw Exception("Input is not a directory: $input")
        inputDir.listFiles()!!.filter { it.extension == "jar" }
    } else {
        listOf(File(input))
    }

    val outputFiles = if (mode == "multiple") {
        val outputDir = File(output)
        if (!outputDir.exists()) throw Exception("Output directory does not exist: $input")
        if (!outputDir.isDirectory) throw Exception("Output is not a directory: $input")
        inputFiles.map {
            File(Paths.get(outputDir.canonicalPath, it.name).toString())
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
                replace
            )
        } catch (e: Exception) {
            if (Logs.task) Logs.finish()
            Logs.error(e.message ?: "Unknown error (${e.javaClass.canonicalName})")
        }
    }
}

fun main(args: Array<String>) {
    File("./.openbukloit/temp").mkdirs()
    try {
        run(args)
    } catch (e: Exception) {
        if (Logs.task) Logs.finish()
        Logs.error(e.message ?: "Unknown error (${e.javaClass.canonicalName})")
    }
    File("./.openbukloit/temp").deleteRecursively()
}
