package com.rs.sentinel.retention

import com.rs.sentinel.type.RiskLevel
import com.rs.sentinel.type.SecurityActionType

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SentinelGuard(
    val minRiskLevel: RiskLevel = RiskLevel.SAFE,
    val action: SecurityActionType = SecurityActionType.EXCEPTION
)

