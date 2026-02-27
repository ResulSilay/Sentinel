package com.rs.kit.hook.detector

import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType


class HookDetector : SecurityDetector {

    init {
        System.loadLibrary("sentinel-hook")
    }

    private external fun isFridaDetected(): Boolean

    override fun detect(): Threat? {
        when {
            isFridaDetected() -> {
                return Threat(
                    type = SecurityType.HOOK,
                    description = "Frida artifacts found in Native memory",
                    severity = 100
                )
            }

            checkStackTraceManually() -> {
                return Threat(
                    type = SecurityType.HOOK,
                    description = "Xposed/LSPosed classes found in StackTrace",
                    severity = 100
                )
            }

            else -> return null
        }
    }

    private fun checkStackTraceManually(): Boolean = runCatching {
        throw Exception()
    }.onFailure { exception ->
        exception.stackTrace.any { element ->
            SentinelConst.HOOK_PACKAGES.any { pkg -> element.className.contains(other = pkg) }
        }
    }.getOrDefault(false)
}