package com.rs.sentinel.type

enum class RiskLevel {
    SAFE,
    LOW,
    MEDIUM,
    HIGH;

    companion object {

        fun getLevel(severity: Int, threshold: Int): RiskLevel = when {
            severity == 0 -> SAFE
            severity >= threshold -> HIGH
            severity >= threshold / 2 -> MEDIUM
            else -> LOW
        }
    }
}