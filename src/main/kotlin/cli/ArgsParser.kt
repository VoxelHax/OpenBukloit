package cli

/**
 * Finds the value associated with a command-line argument.
 *
 * @param full The full name of the argument (e.g., "input").
 * @param short The short name of the argument (e.g., "i"), or null if none.
 * @param args The array of command-line arguments.
 * @param last If true, returns the value for the last occurrence of the argument; otherwise, returns the first.
 * @return The value of the argument, or null if the argument is not found or has no value.
 */
fun findArg(full: String, short: String? = null, args: Array<String>, last: Boolean = false): String? {
    val indices = args.indices.filter { i -> args[i] == "--$full" || (short != null && args[i] == "-$short") }
    if (indices.isEmpty()) return null

    val index = if (last) indices.last() else indices.first()
    return args.getOrNull(index + 1)
}

/**
 * Checks if a boolean flag argument is present in the command-line arguments.
 *
 * @param full The full name of the flag (e.g., "replace").
 * @param short The short name of the flag (e.g., "r"), or null if none.
 * @param args The array of command-line arguments.
 * @return True if the flag is present, false otherwise.
 */
fun boolArg(full: String, short: String? = null, args: Array<String>): Boolean {
    return args.any { it == "--$full" || (short != null && it == "-$short") }
}
