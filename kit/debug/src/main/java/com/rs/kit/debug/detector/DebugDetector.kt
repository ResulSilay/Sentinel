package com.rs.kit.debug.detector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug.isDebuggerConnected
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation

class DebugDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> {
        val isDebugger = isDebuggerConnected()
        val isDebuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        val isTestKeys = Build.TAGS?.contains(other = SentinelConst.TEST_KEYS_TAG) == true

        return buildList {
            if (isDebugger || isDebuggable) {
                add(
                    Threat(
                        violation = SecurityViolation.Debugger.Debuggable
                    )
                )
            }

            if (isTestKeys) {
                add(
                    Threat(
                        violation = SecurityViolation.Debugger.TestKeys
                    )
                )
            }
        }
    }
}