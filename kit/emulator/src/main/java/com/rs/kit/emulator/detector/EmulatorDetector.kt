package com.rs.kit.emulator.detector

import android.os.Build
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation
import java.io.File

class EmulatorDetector : SecurityDetector {

    override fun detect(): List<Threat> {
        val buildDetails = (
                Build.FINGERPRINT
                        + Build.DEVICE
                        + Build.MODEL
                        + Build.BRAND
                        + Build.PRODUCT
                        + Build.MANUFACTURER
                        + Build.HARDWARE
                ).lowercase()

        val pipe = SentinelConst.EMULATOR_PIPES.firstOrNull { File(it).exists() }
        val prop = SentinelConst.EMULATOR_PROPS.firstOrNull { buildDetails.contains(it) }

        return buildList {
            if (pipe != null) {
                add(
                    Threat(
                        violation = SecurityViolation.Emulator.Detected(name = "Pipe: $pipe")
                    )
                )
            }

            if (prop != null) {
                add(
                    Threat(
                        violation = SecurityViolation.Emulator.Detected(name = "Prop: $prop")
                    )
                )
            }
        }
    }
}