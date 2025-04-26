package utils

import java.util.*

/**
 * Generates a random lowercase string of a specified length.
 *
 * @param length The desired length of the string.
 * @return A random lowercase string.
 */
fun makeRandomLowerCaseString(length: Int): String {
    return (0 until length)
        .map { kotlin.random.Random.nextInt(0, 26) } // Generate random numbers corresponding to lowercase letters
        .map { 'a' + it } // Convert numbers to lowercase characters
        .joinToString("") // Join characters into a string
}
