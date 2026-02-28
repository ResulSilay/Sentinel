package com.rs.sentinel.detector

import com.rs.sentinel.violation.SecurityViolation

data class Threat(
    val violation: SecurityViolation
)