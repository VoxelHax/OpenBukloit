package utils

import java.io.File
import java.net.URI
import javassist.ClassPool

private val currentJar: File by lazy {
    File(ClassPool::class.java.protectionDomain.codeSource.location.toURI())
}

/**
 * Determines the correct classpath argument for compilation or injection.
 *
 * If the current code is running from a directory (e.g., during development),
 * it uses the provided [classpath]. If it's running from a JAR, it uses
 * the path to the current JAR file.
 *
 * @param classpath The existing classpath string.
 * @return The determined classpath argument.
 */
fun getClassPathArg(classpath: String): String {
    return if (currentJar.isDirectory) classpath else currentJar.absolutePath
}
