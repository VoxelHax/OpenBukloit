package utils

import cli.Logs
import dev.virefire.yok.Yok
import org.rauschig.jarchivelib.ArchiverFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Converts Java class file major and minor versions to a human-readable Java version string.
 *
 * @param major The major version number.
 * @param minor The minor version number.
 * @return The corresponding Java version string (e.g., "1.8", "11", "17").
 */
fun getJavaVersion(major: Int, minor: Int): String {
    return when (major to minor) {
        45 to 0 -> "1.0"
        45 to 3 -> "1.1"
        46 to 0 -> "1.2"
        47 to 0 -> "1.3"
        48 to 0 -> "1.4"
        else -> (major - 44).toString() // For Java 5 and above, version is major - 44
    }
}

/**
 * Determines the current operating system type.
 *
 * @return A lowercase string representing the OS type ("mac", "windows", "linux", or the first word of the OS name).
 */
fun getOsType(): String {
    val os = System.getProperty("os.name", "generic").lowercase()
    return when {
        os.contains("mac") || os.contains("darwin") -> "mac"
        os.contains("win") -> "windows"
        os.contains("nux") -> "linux"
        else -> os.split(" ")[0]
    }
}

/**
 * Ensures that a compatible JDK is available for a given Java class file version.
 * If the JDK is not found locally, it attempts to download and install it from Adoptium.
 *
 * @param major The major version of the class file.
 * @param minor The minor version of the class file.
 * @return The Path to the 'javac' executable of the required JDK.
 * @throws Exception if the class version is not supported or the JDK cannot be found/installed.
 */
fun requireJDK(major: Int, minor: Int): Path {
    // OpenBukloit supports Java 8 (major version 52) and above for compilation.
    if (major < 52) {
        throw Exception("Class version $major.$minor is not supported by OpenBukloit. Minimum supported version is Java 8 (52.0).")
    }

    val versionId = getJavaVersion(major, minor)
    val jdkDir = File("./.openbukloit/jdk/$versionId")
    val currentOs = getOsType()

    // Check for existing javac executable based on OS
    val existingJavacPath = when (currentOs) {
        "mac" -> Paths.get(jdkDir.canonicalPath, "Contents", "Home", "bin", "javac")
        "windows" -> Paths.get(jdkDir.canonicalPath, "bin", "javac.exe")
        else -> Paths.get(jdkDir.canonicalPath, "bin", "javac")
    }

    if (existingJavacPath.toFile().exists()) {
        Logs.info("Found existing JDK $versionId at ${jdkDir.canonicalPath}")
        return existingJavacPath
    }

    Logs.info("JDK $versionId not found at ${jdkDir.canonicalPath}, downloading (this may take some time)...")

    // Determine system architecture for download
    var currentArchitecture = System.getProperty("os.arch").lowercase()
    currentArchitecture = when (currentArchitecture) {
        "x86_64", "amd64" -> "x64"
        "i386" -> "x32"
        "aarch64" -> "aarch64"
        else -> currentArchitecture
    }

    // Fallback cause Adoptium does not provide native macOS aarch64 builds for Java less than 11
    if (currentOs == "mac" && currentArchitecture == "aarch64" && major < 55) {
        Logs.info("Adoptium does not support macOS aarch64 for Java $versionId. Falling back to x64 build...")
        currentArchitecture = "x64"
    }

    // Determine archive extension based on OS
    val extension = if (currentOs == "windows") ".zip" else ".tar.gz"
    val binaryName = "jdk-$versionId-$currentOs-$currentArchitecture$extension"
    // Construct Adoptium API download URL
    val url = "https://api.adoptium.net/v3/binary/latest/$versionId/ga/$currentOs/$currentArchitecture/jdk/hotspot/normal/eclipse?project=jdk"

    // Create necessary directories for download and installation
    File("./.openbukloit/temp/jdk").mkdirs()
    jdkDir.mkdirs()

    val downloadedFilePath = Paths.get("./.openbukloit/temp/jdk", binaryName)

    try {
        // Download the JDK binary
        Logs.info("Downloading JDK from $url to ${downloadedFilePath.toFile().canonicalPath}")
        Files.copy(Yok.get(url).body.stream, downloadedFilePath)

        Logs.info("JDK $versionId downloaded, extracting to ${jdkDir.canonicalPath}...")

        // Extract the downloaded archive
        val archiver = if (extension == ".tar.gz") ArchiverFactory.createArchiver("tar", "gz") else ArchiverFactory.createArchiver("zip")
        archiver.extract(downloadedFilePath.toFile(), jdkDir)

        // Move contents up one level if extracted into a subdirectory
        val extractedContents = jdkDir.listFiles()
        if (extractedContents != null && extractedContents.size == 1 && extractedContents[0].isDirectory) {
            Logs.info("Adjusting extracted directory structure...")
            copyDirectory(extractedContents[0], jdkDir)
            extractedContents[0].deleteRecursively()
        }

        Logs.info("JDK $versionId installed successfully")

        // Re-check for javac executable after extraction
        if (existingJavacPath.toFile().exists()) {
            return existingJavacPath
        } else {
            throw Exception("JDK $versionId installed but 'javac' executable not found at expected location: ${existingJavacPath.toFile().canonicalPath}")
        }

    } catch (e: Exception) {
        // Clean up downloaded file and directory on failure
        if (downloadedFilePath.toFile().exists()) downloadedFilePath.toFile().delete()
        if (jdkDir.exists()) jdkDir.deleteRecursively()
        throw Exception("Failed to download or install JDK $versionId: ${e.message}", e)
    } finally {
        // Clean up temporary download directory
        val tempJdkDir = File("./.openbukloit/temp/jdk")
        if (tempJdkDir.exists()) tempJdkDir.deleteRecursively()
    }
}

/**
 * Finds the path to the Java Runtime Environment (JRE) executable associated with a JDK path.
 *
 * @param jdkPath The Path to the 'javac' or 'javac.exe' executable within the JDK bin directory.
 * @return The Path to the 'java' or 'java.exe' executable.
 * @throws Exception if the JRE executable is not found.
 */
/**
 * Finds the path to the Java Runtime Environment (JRE) executable associated with a JDK path.
 *
 * @param jdkPath The Path to the 'javac' or 'javac.exe' executable within the JDK bin directory.
 * @return The Path to the 'java' or 'java.exe' executable.
 * @throws Exception if the JRE executable is not found.
 */
fun toJRE(jdkPath: Path): Path {
    val binDir = jdkPath.parent.toFile() // Get the bin directory
    val binDirPath = binDir.canonicalPath // Use canonicalPath from File

    val jrePath = Paths.get(binDirPath, "java")
    if (jrePath.toFile().exists()) {
        return jrePath
    }
    val jreExePath = Paths.get(binDirPath, "java.exe")
    if (jreExePath.toFile().exists()) {
        return jreExePath
    }
    throw Exception("JRE executable ('java' or 'java.exe') not found in $binDirPath")
}
