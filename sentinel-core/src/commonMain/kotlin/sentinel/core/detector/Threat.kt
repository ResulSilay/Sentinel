package sentinel.core.detector

import sentinel.core.violation.SecurityViolation

data class Threat(
    val violation: SecurityViolation
)