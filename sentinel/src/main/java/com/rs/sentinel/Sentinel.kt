package com.rs.sentinel

import android.content.Context
import android.location.Location
import com.rs.kit.debug.detector.DebugDetector
import com.rs.kit.emulator.detector.EmulatorDetector
import com.rs.kit.hook.detector.HookDetector
import com.rs.kit.location.detector.MockLocationAppDetector
import com.rs.kit.location.detector.MockLocationDetector
import com.rs.kit.location.detector.MockLocationSettingDetector
import com.rs.kit.root.detector.RootDetector
import com.rs.kit.tamper.detector.TamperDetector
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.SecurityReport
import com.rs.sentinel.type.RiskLevel

class Sentinel private constructor(
    private val detectors: List<SecurityDetector>,
    private val threshold: Int,
) {

    fun inspect(): SecurityReport {
        val threats = detectors.mapNotNull { detector -> detector.detect() }
        val totalScore = threats.sumOf { threat -> threat.severity }

        val level = RiskLevel.fromScore(
            score = totalScore,
            threshold = threshold
        )

        return SecurityReport(
            score = totalScore,
            threats = threats,
            riskLevel = level
        )
    }

    class Builder(
        private val context: Context,
    ) {

        private val detectors = mutableListOf<SecurityDetector>()

        private val config = Config(
            threshold = DEFAULT_THRESHOLD
        )

        fun config(block: Config.() -> Unit) {
            config.apply(block)
        }

        fun root() {
            detectors.add(RootDetector(context = context))
        }

        fun tamper() {
            detectors.add(
                TamperDetector(
                    context = context,
                    packageName = config.packageName,
                    signature = config.signature
                )
            )
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

        fun location() {
            detectors.add(MockLocationSettingDetector(context = context))
            detectors.add(MockLocationAppDetector(context = context))
        }

        fun location(location: Location) {
            detectors.add(MockLocationDetector(location = location))
        }

        fun all() {
            root()
            tamper()
            emulator()
            debug()
            hook()
            location()
        }

        fun build() = Sentinel(
            detectors = detectors.toList(),
            threshold = config.threshold
        )
    }

    companion object {

        private const val DEFAULT_THRESHOLD = 80

        inline fun configure(
            context: Context,
            block: Builder.() -> Unit,
        ): Sentinel = Builder(context = context).apply(block).build()
    }
}