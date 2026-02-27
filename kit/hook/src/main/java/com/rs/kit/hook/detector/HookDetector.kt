package com.rs.kit.hook.detector

import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.violation.SecurityViolation

class HookDetector : SecurityDetector {

    init {
        System.loadLibrary("sentinel-hook")
    }

    private external fun isFridaDetected(): Boolean

    override fun detect(): Threat? {
        val (isCheckStackTraceManually, name) =  checkStackTraceManually()

        return when {
            isFridaDetected() -> {
                Threat(
                    violation = SecurityViolation.Hook.FridaDetected
                )
            }

            isCheckStackTraceManually -> {
                Threat(
                    violation = SecurityViolation.Hook.FrameworkDetected(name = name)
                )
            }

            else -> null
        }
    }

    private fun checkStackTraceManually(): Pair<Boolean, String?> = runCatching {
        throw Exception()
    }.onFailure { exception ->
        val detectedPackage = exception.stackTrace.firstNotNullOfOrNull { element ->
            SentinelConst.HOOK_PACKAGES.firstOrNull { pkg ->
                element.className.contains(pkg)
            }
        }

        (detectedPackage != null) to detectedPackage
    }.getOrElse {
        false to null
    }
}