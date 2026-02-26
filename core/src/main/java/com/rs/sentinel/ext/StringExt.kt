package com.rs.sentinel.ext

fun String.toByteList(): List<Byte> = toByteArray(Charsets.UTF_8).toList()