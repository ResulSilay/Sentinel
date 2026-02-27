package com.rs.sentinel.type

enum class RiskLevel {
    SAFE,
    LOW,
    MEDIUM,
    HIGH;

    companion object {

        fun fromScore(score: Int, threshold: Int): RiskLevel {
            val totalPenalty = 100 - score

            return when {
                totalPenalty == 0 -> SAFE
                totalPenalty >= threshold -> HIGH
                totalPenalty >= threshold / 2 -> MEDIUM
                else -> LOW
            }
        }
    }
}