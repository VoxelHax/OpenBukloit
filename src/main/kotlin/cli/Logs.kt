package cli

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*

object Logs {
    var task = false
        private set
    private var taskFinish = false
    fun task(msg: String) {
        println(" ${brightMagenta("╓")} ${brightGreen(bold(msg))}")
        task = true
    }

    fun finish(): Logs {
        taskFinish = true
        return this
    }

    private fun log(message: String) {
        if (task) {
            if (taskFinish) {
                taskFinish = false
                task = false
                println(" ${brightMagenta("╙")} $message")
            } else {
                println(" ${brightMagenta("║")} $message")
            }
        } else {
            println(message)
        }
    }

    fun info(message: String) = log(brightWhite(message))
    fun warn(message: String) = log(brightYellow(message))
    fun error(message: String) = log(brightRed(message))
}
