package com.rs.kit.emulator.detector

import android.os.Build
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType
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

        val propCheck = SentinelConst.EMULATOR_PROPS.any { buildDetails.contains(other = it) }
        val pipeCheck = SentinelConst.EMULATOR_PIPES.any { File(it).exists() }

        return if (propCheck || pipeCheck) {
            Threat(
                type = SecurityType.EMULATOR,
                description = "Emulator environment detected",
                severity = 30
            )
        } else null
    }
}