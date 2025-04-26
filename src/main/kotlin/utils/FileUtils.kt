package utils

import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Copies a directory recursively from a source location to a target location.
 *
 * @param sourceLocation The source directory to copy.
 * @param targetLocation The target directory to copy to.
 */
fun copyDirectory(sourceLocation: File, targetLocation: File) {
    Files.walk(sourceLocation.toPath())
        .forEach { source ->
            val destination: Path = Paths.get(
                targetLocation.path, source.toFile().canonicalPath
                    .substring(sourceLocation.canonicalPath.length)
            )
            if (source.toFile().isDirectory) {
                Files.createDirectories(destination)
            } else {
                Files.copy(source, destination)
            }
        }
}
