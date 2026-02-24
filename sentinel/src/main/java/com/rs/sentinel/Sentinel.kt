package com.rs.sentinel

import android.content.Context
import com.rs.kit.debug.detector.DebugDetector
import com.rs.kit.emulator.detector.EmulatorDetector
import com.rs.kit.hook.detector.HookDetector
import com.rs.kit.root.detector.RootDetector
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.SecurityReport
import com.rs.sentinel.type.RiskLevel
import kotlin.collections.mapNotNull

class Sentinel private constructor(
    private val detectors: List<SecurityDetector>,
    private val threshold: Int,
) {

    fun inspect(): SecurityReport {
        val threats = detectors.mapNotNull { detector -> detector.detect() }
        val totalScore = threats.sumOf { threat -> threat.severity }

        val level = RiskLevel.fromScore(
            score = totalScore,
            threshold = this.threshold
        )

        return SecurityReport(
            score = totalScore,
            threats = threats,
            riskLevel = level
        )
    }

    class Builder(private val context: Context) {

        private val detectors = mutableListOf<SecurityDetector>()

        private var threshold: Int = 80

        fun root() {
            detectors.add(RootDetector(context = context))
        }

        fun emulator() {
            detectors.add(EmulatorDetector())
        }

        fun debug() {
            detectors.add(DebugDetector(context = context))
        }

        fun hook() {
            detectors.add(HookDetector())
        }

        fun all() {
            root()
            emulator()
            debug()
            hook()
        }

        fun build() = Sentinel(
            detectors = detectors.toList(),
            threshold = threshold
        )
    }

    companion object {

        inline fun configure(
            context: Context,
            block: Builder.() -> Unit,
        ): Sentinel = Builder(context = context).apply(block).build()
    }
}