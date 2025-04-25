package utils

import java.io.File
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import kotlin.random.Random

data class Camouflage(
    val className: String,
    val methodName: String,
)

fun splitCamelCase(s: String): List<String> {
    return s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])".toRegex())
}

fun uniqueName(dict: List<String>, files: List<String>, length: IntRange): String {
    var name: String
    var depth = 0
    do {
        name = (1..kotlin.random.Random.nextInt(length.first, length.last + 1))
            .map { dict[kotlin.random.Random.nextInt(dict.size)] }
            .joinToString("")
        if (depth > 10) name += depth.toString()
        depth++
    } while (files.contains(name))
    return name
}

fun calculateCamouflage(file: File): Camouflage {
    val fileTree = mutableMapOf<String, MutableList<String>>()
    ZipFile(file.canonicalPath).use { zipFile ->
        val zipEntries: Enumeration<*> = zipFile.entries()
        while (zipEntries.hasMoreElements()) {
            val zipEntry = zipEntries.nextElement() as ZipEntry
            if (zipEntry.isDirectory) continue
            val fileName: String = zipEntry.name
            val dir = fileName.substringBeforeLast("/")
            val name = fileName.substringAfterLast("/")
            if (!name.endsWith(".class")) continue
            if (!fileTree.containsKey(dir)) {
                fileTree[dir] = mutableListOf()
            }
            fileTree[dir]!!.add(name)
        }
    }
    val asList = fileTree.toList().sortedBy { -it.second.size }
    val intoDir = Random.nextInt((asList.size - 1).coerceAtMost(3))
    val dir = asList[intoDir].first
    val files = asList[intoDir].second

    val dict = mutableListOf<String>()
    for (f in files) {
        val fileName = f.substringBeforeLast(".")
        splitCamelCase(fileName)
            .map { it.replace("$", "") }
            .filter { it.length > 2 }
            .forEach { if (!dict.contains(it)) dict.add(it) }
    }

    val className = dir + "/" + uniqueName(dict, files.map { it.substringBeforeLast(".") }, 2..3)
    var methodName = uniqueName(dict, files.map { it.substringBeforeLast(".") }, 1..2)
    methodName = methodName.replaceFirstChar { if (it.isUpperCase()) it.lowercase() else it.toString() }

    return Camouflage(className, methodName)
}
