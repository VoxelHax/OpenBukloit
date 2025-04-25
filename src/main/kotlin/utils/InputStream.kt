package utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun getBytesFromInputStream(stream: InputStream): ByteArray {
    return stream.readBytes()
}
