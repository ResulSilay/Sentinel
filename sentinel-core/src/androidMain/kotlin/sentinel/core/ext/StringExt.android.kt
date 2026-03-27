package sentinel.core.ext

actual fun String.toByteList(): List<Byte> = toByteArray(Charsets.UTF_8).toList()