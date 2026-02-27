package com.rs.sentinel.model

import com.rs.sentinel.violation.SecurityViolation

data class Threat(
    val violation: SecurityViolation
)