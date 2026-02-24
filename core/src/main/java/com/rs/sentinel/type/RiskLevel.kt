package com.rs.sentinel.type

enum class RiskLevel {
    SAFE,
    MEDIUM,
    HIGH;

    companion object {

        fun fromScore(score: Int, threshold: Int = 80): RiskLevel = when {
            score == 0 -> SAFE
            score >= threshold -> HIGH
            else -> MEDIUM
        }
    }
}