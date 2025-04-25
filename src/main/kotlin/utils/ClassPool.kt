package utils

import java.io.File

private val currentJar = File(ClassPool::class.java.protectionDomain.codeSource.location.toURI())

fun getClassPathArg(classpath: String): String {
    return if (currentJar.isDirectory) classpath else currentJar.toString()
}
