package com.rs.sentinel.ext

fun List<Byte>.toHexString(): String = joinToString(separator = "") { "%02X".format(it) }
