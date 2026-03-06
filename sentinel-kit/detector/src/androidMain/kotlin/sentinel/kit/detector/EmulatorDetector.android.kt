package sentinel.kit.detector

import android.os.Build
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.SecurityViolation
import sentinel.kit.detector.constant.DetectorConst
import java.io.File

actual class EmulatorDetector : SecurityDetector {

    actual override fun detect(): List<Threat> {
        val (pipe, prop) = isEmulator()

        return buildList {
            if (pipe != null) {
                add(
                    element = Threat(
                        violation = SecurityViolation.Emulator.Detected(name = "Pipe: $pipe")
                    )
                )
            }

            if (prop != null) {
                add(
                    element = Threat(
                        violation = SecurityViolation.Emulator.Detected(name = "Prop: $prop")
                    )
                )
            }
        }
    }

    private fun isEmulator(): Pair<String?, String?> {
        val buildDetails = (
                Build.FINGERPRINT
                        + Build.DEVICE
                        + Build.MODEL
                        + Build.BRAND
                        + Build.PRODUCT
                        + Build.MANUFACTURER
                        + Build.HARDWARE
                ).lowercase()

        val pipe = DetectorConst.EMULATOR_PIPES.firstOrNull { File(it).exists() }
        val prop = DetectorConst.EMULATOR_PROPS.firstOrNull { buildDetails.contains(it) }
        return Pair(pipe, prop)
    }
}