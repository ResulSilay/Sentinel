package com.rs.sentinel.model

import com.rs.sentinel.type.RiskLevel
import com.rs.sentinel.violation.SecurityViolation
import kotlin.reflect.KClass

data class SecurityReport(
    val threats: List<Threat>,
    val threshold: Int,
    val timestamp: Long = System.currentTimeMillis(),
) {
    val score: Int by lazy {
        val totalPenalty = threats.sumOf { it.violation.severity }
        (100 - totalPenalty).coerceIn(0, 100)
    }

    val riskLevel: RiskLevel by lazy {
        RiskLevel.fromScore(
            score = score,
            threshold = threshold
        )
    }

    fun isSafe(): Boolean = riskLevel == RiskLevel.SAFE

    fun isCritical(): Boolean = riskLevel == RiskLevel.HIGH

    fun hasViolationCategory(category: KClass<out SecurityViolation>): Boolean {
        return threats.any { category.isInstance(it.violation) }
    }

    inline fun <reified T : SecurityViolation> hasViolation(): Boolean {
        return threats.any { it.violation is T }
    }
}