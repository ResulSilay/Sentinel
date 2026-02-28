package com.rs.kit.location.detector

import android.content.Context
import android.content.pm.PackageManager
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
import com.rs.sentinel.violation.SecurityViolation

class MockLocationAppDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> {
        val mockLocationPackages = detectMockLocationPackages()

        return buildList {
            if (mockLocationPackages.isNotEmpty()) {
                add(
                    Threat(
                        violation = SecurityViolation.Location.MockAppInstalled(packages = mockLocationPackages)
                    )
                )
            }
        }
    }

    private fun detectMockLocationPackages(): List<String> {
        val packageManager = context.packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)

        return packages.filter { pkg ->
            val hasMockPermission = pkg.requestedPermissions?.contains(
                element = "android.permission.ACCESS_MOCK_LOCATION"
            ) == true

            val isNotSelf = pkg.packageName != context.packageName

            hasMockPermission && isNotSelf
        }.map { it.packageName }
    }
}