package sentinel.core.ext

expect fun String?.toByteList(): List<Byte>

internal fun Any.categoryOf(): String? =
    this::class.qualifiedName
        ?.split(".")
        ?.takeLast(2)
        ?.firstOrNull()