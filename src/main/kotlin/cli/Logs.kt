package cli

import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.rendering.TextStyles.*

object Logs {
    // Indicates if a task is currently being logged.
    var task = false
        private set

    // Indicates if the current task is being finished.
    private var taskFinish = false

    /**
     * Starts logging a new task with a specified message.
     *
     * @param msg The message describing the task.
     */
    fun task(msg: String) {
        println(" ${brightMagenta("╓")} ${brightGreen(bold(msg))}")
        task = true
    }

    /**
     * Marks the current task as finishing. Subsequent log messages will be
     * treated as the final message for the task.
     *
     * @return The Logs object for chaining.
     */
    fun finish(): Logs {
        taskFinish = true
        return this
    }

    /**
     * Internal function to log a message, handling task-specific formatting.
     *
     * @param message The message to log.
     */
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

    /**
     * Logs an informational message.
     *
     * @param message The info message.
     */
    fun info(message: String) = log(brightWhite(message))

    /**
     * Logs a warning message.
     *
     * @param message The warning message.
     */
    fun warn(message: String) = log(brightYellow(message))

    /**
     * Logs an error message.
     *
     * @param message The error message.
     */
    fun error(message: String) = log(brightRed(message))
}
