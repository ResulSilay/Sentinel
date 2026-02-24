package com.rs.sentinel.model

import com.rs.sentinel.type.SecurityType

data class Threat(
    val type: SecurityType,
    val description: String,
    val severity: Int,
)