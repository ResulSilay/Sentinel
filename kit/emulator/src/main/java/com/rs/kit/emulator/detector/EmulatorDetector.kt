package com.rs.kit.emulator.detector

import android.os.Build
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.violation.SecurityViolation
import java.io.File

class EmulatorDetector : SecurityDetector {

    override fun detect(): Threat? {
        val buildDetails = (
                Build.FINGERPRINT
                        + Build.DEVICE
                        + Build.MODEL
                        + Build.BRAND
                        + Build.PRODUCT
                        + Build.MANUFACTURER
                        + Build.HARDWARE
                ).lowercase()

        val foundProp = SentinelConst.EMULATOR_PROPS.firstOrNull { buildDetails.contains(it) }
        val foundPipe = SentinelConst.EMULATOR_PIPES.firstOrNull { File(it).exists() }

        return when {
            foundPipe != null -> {
                Threat(
                    violation = SecurityViolation.Emulator.Detected(name = "Pipe: $foundPipe"),
                )
            }

            foundProp != null -> {
                Threat(
                    violation = SecurityViolation.Emulator.Detected(name = "Prop: $foundProp"),
                )
            }

            else -> null
        }
    }
}