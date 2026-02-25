package com.rs.kit.location.detector

import android.content.Context
import android.content.pm.PackageManager
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType

class MockLocationAppDetector(
    private val context: Context,
) : SecurityDetector {

    private val mockLocationApps = mutableListOf<String>()

    override fun detect(): Threat? = when {
        getSuspiciousApps().isNotEmpty() -> Threat(
            type = SecurityType.LOCATION,
            description = "Install mock location app.",
            severity = 10
        )

        else -> null
    }

    private fun getSuspiciousApps(): List<String> {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        mockLocationApps.clear()

        for (pkg in packages) {
            val permissions = pkg.requestedPermissions
            if (permissions != null && permissions.contains("android.permission.ACCESS_MOCK_LOCATION")) {
                if (pkg.packageName != context.packageName) {
                    mockLocationApps.add(pkg.packageName)
                }
            }
        }
        return mockLocationApps
    }
}