package com.rs.sentinel.model

import com.rs.sentinel.type.RiskLevel
import com.rs.sentinel.type.SecurityType

data class SecurityReport(
    val score: Int,
    val threats: List<Threat>,
    val riskLevel: RiskLevel,
) {
    fun isSafe(): Boolean = riskLevel == RiskLevel.SAFE

    fun isCritical(): Boolean = riskLevel == RiskLevel.HIGH

    fun hasThreatType(type: SecurityType): Boolean = threats.any { it.type == type }
}