package utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * Reads all bytes from an InputStream and returns them as a ByteArray.
 *
 * @param stream The InputStream to read from.
 * @return A ByteArray containing all bytes from the stream.
 */
fun getBytesFromInputStream(stream: InputStream): ByteArray {
    return stream.readBytes()
}
