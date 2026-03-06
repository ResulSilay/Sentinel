package sentinel.kit.detector

import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.SecurityViolation
import sentinel.kit.detector.constant.DetectorConst

actual class EmulatorDetector : SecurityDetector {

    actual override fun detect(): List<Threat> = buildList {
        if (isSimulator()) {
            add(
                element = Threat(
                    violation = SecurityViolation.Emulator.Detected()
                )
            )
        }
    }

    private fun isSimulator(): Boolean {
        val model = UIDevice.currentDevice.model
        val env = NSProcessInfo.processInfo.environment

        val isSimulatorModel = DetectorConst.SIMULATOR_MODEL_KEYS.any { modelKey ->
            model.contains(other = modelKey, ignoreCase = true)
        }
        val hasSimulatorEnv = DetectorConst.SIMULATOR_KEYS.any { key -> env[key] != null }

        return isSimulatorModel || hasSimulatorEnv
    }
}