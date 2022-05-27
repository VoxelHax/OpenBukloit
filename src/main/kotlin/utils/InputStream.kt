package utils

import java.io.ByteArrayOutputStream
import java.io.InputStream

fun getBytesFromInputStream(stream: InputStream): ByteArray {
    val os = ByteArrayOutputStream()
    val buffer = ByteArray(0xFFFF)
    var len = stream.read(buffer)
    while (len != -1) {
        os.write(buffer, 0, len)
        len = stream.read(buffer)
    }
    return os.toByteArray()
}
