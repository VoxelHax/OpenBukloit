package cli

fun findArg(full: String, short: String? = null, args: Array<String>, last: Boolean = false): String? {
    val indices = args.indices.filter { i -> args[i] == "--$full" || (short != null && args[i] == "-$short") }
    if (indices.isEmpty()) return null

    if (!last) {
        val index = indices.first()
        return if (index + 1 < args.size) args[index + 1] else null
    } else {
        val index = indices.last()
        return if (index + 1 < args.size) args[index + 1] else null
    }
}

fun boolArg(full: String, short: String? = null, args: Array<String>): Boolean {
    for (i in args.indices) {
        if (args[i] == "--$full" || (short != null && args[i] == "-$short")) {
            return true
        }
    }
    return false
}
