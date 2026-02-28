package com.rs.kit.location.detector

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation

class MockLocationSettingDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isMockSettingEnabled()) {
            add(
                Threat(
                    violation = SecurityViolation.Location.MockSettingEnabled
                )
            )
        }
    }

    private fun isMockSettingEnabled(): Boolean {
        val opsManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        return runCatching {
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                @Suppress("DEPRECATION")
                opsManager.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_MOCK_LOCATION,
                    android.os.Process.myUid(),
                    context.packageName
                )
            } else {
                opsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_MOCK_LOCATION,
                    android.os.Process.myUid(),
                    context.packageName
                )
            }
            result == AppOpsManager.MODE_ALLOWED
        }.getOrDefault(false)
    }
}