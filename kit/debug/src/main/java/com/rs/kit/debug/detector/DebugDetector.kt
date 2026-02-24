package com.rs.kit.debug.detector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug.isDebuggerConnected
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType

class DebugDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): Threat? {
        val isDebugger = isDebuggerConnected()
        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        val isTestKeys = Build.TAGS?.contains(other = SentinelConst.TEST_KEYS_TAG) == true

        return when {
            isDebugger || isDebuggable -> Threat(
                type = SecurityType.DEBUGGER,
                description = "Debugger active",
                severity = 80
            )

            isTestKeys -> Threat(
                type = SecurityType.DEBUGGER,
                description = "Test-keys build",
                severity = 30
            )

            else -> null
        }
    }
}