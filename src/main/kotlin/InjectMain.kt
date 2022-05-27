import injector.injectFunc
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val clazz = args[0]
    val method = args[1]
    val insert = args[2]
    val saveTo = args[3]
    try {
        injectFunc(clazz, method, insert, saveTo)
    } catch (e: Exception) {
        System.err.println(e.message)
        exitProcess(1)
    }
}
