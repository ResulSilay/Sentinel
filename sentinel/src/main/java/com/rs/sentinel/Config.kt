package com.rs.sentinel

data class Config(
    var packageName: List<Byte>? = null,
    var packageSignature: List<Byte>? = null,
    var threshold: Int,
)