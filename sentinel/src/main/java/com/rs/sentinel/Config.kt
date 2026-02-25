package com.rs.sentinel

data class Config(
    var packageName: List<Byte>?? = null,
    var signature: List<Byte>?? = null,
    var threshold: Int,
)