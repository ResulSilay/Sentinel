package com.rs.sentinel.report

import com.rs.sentinel.detector.Threat
import com.rs.sentinel.type.RiskLevel
import com.rs.sentinel.violation.SecurityViolation
import kotlin.reflect.KClass

data class SecurityReport(
    val threats: List<Threat>,
    val threshold: Int,
    val timestamp: Long = System.currentTimeMillis(),
) {
    val severity: Int by lazy {
        threats.sumOf { threat -> threat.violation.severity }
    }

    val riskLevel: RiskLevel by lazy {
        RiskLevel.getLevel(
            severity = severity,
            threshold = threshold
        )
    }

    fun isSafe(): Boolean = riskLevel == RiskLevel.SAFE

    fun isCritical(): Boolean = riskLevel == RiskLevel.HIGH

    fun hasViolationCategory(category: KClass<out SecurityViolation>): Boolean {
        return threats.any { threat -> category.isInstance(threat.violation) }
    }

    inline fun <reified T : SecurityViolation> hasViolation(): Boolean {
        return threats.any { threat -> threat.violation is T }
    }
}