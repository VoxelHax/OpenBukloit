package utils

import java.util.*

fun makeRandomLowerCaseString(length: Int): String {
    val random = Random()
    val sb = StringBuilder(length)
    for (i in 0 until length) {
        sb.append((random.nextInt(26) + 'a'.code).toChar())
    }
    return sb.toString()
}
