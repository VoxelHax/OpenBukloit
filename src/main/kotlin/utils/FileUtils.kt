package utils

import java.io.*

fun copyDirectory(sourceLocation: File, targetLocation: File) {
    if (sourceLocation.isDirectory) {
        if (!targetLocation.exists()) {
            targetLocation.mkdir()
        }
        val children: Array<String> = sourceLocation.list() as Array<String>
        for (i in children.indices) {
            copyDirectory(
                File(sourceLocation, children[i]),
                File(targetLocation, children[i])
            )
        }
    } else {
        val `in`: InputStream = FileInputStream(sourceLocation)
        val out: OutputStream = FileOutputStream(targetLocation)

        // Copy the bits from instream to outstream
        val buf = ByteArray(1024)
        var len: Int
        while (`in`.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        `in`.close()
        out.close()
    }
}
