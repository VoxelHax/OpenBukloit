package utils

import java.util.*

fun makeRandomLowerCaseString(length: Int): String {
    return (0 until length)
        .map { kotlin.random.Random.nextInt(0, 26) }
        .map { 'a' + it }
        .joinToString("")
}
