package cli

fun findArg(full: String, short: String? = null, args: Array<String>, last: Boolean = false): String? {
    var result: String? = null
    for (i in args.indices) {
        if (args[i] == "--$full" || (short != null && args[i] == "-$short")) {
            if (i + 1 < args.size) {
                if (last)
                    result = args[i + 1]
                else
                    return args[i + 1]
            }
        }
    }
    return result
}

fun boolArg(full: String, short: String? = null, args: Array<String>): Boolean {
    for (i in args.indices) {
        if (args[i] == "--$full" || (short != null && args[i] == "-$short")) {
            return true
        }
    }
    return false
}
