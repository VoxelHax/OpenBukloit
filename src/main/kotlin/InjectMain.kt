import injector.injectFunc
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val clazz = args[0] // Target class name
    val method = args[1] // Target method name
    val insert = args[2] // Code to insert
    val saveTo = args[3] // Path to save the modified class file

    try {
        // Attempt to inject the code into the target method of the target class.
        injectFunc(clazz, method, insert, saveTo)
        exitProcess(0) // Indicate success
    } catch (e: Exception) {
        // Print error message and exit with a non-zero status code on failure.
        System.err.println("Error during injection: ${e.message}")
        exitProcess(1) // Indicate failure
    }
}
