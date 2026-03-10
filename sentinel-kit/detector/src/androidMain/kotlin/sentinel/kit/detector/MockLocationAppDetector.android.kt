package sentinel.kit.detector

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation
import sentinel.kit.detector.constant.DetectorConst

class MockLocationAppDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> {
        val mockLocationPackages = detectMockLocationPackages()

        return buildList {
            if (mockLocationPackages.isNotEmpty()) {
                add(
                    element = Threat(
                        violation = AndroidViolation.Location.MockAppInstalled(packages = mockLocationPackages)
                    )
                )
            }
        }
    }

    private fun detectMockLocationPackages(): List<String> {
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages()

        return installedPackages.filter { packageInfo ->
            val packageName = packageInfo.packageName
            val hasMockLocationPackage = packageName in DetectorConst.MOCK_LOCATION_PACKAGES
            val hasMockPermission =
                packageInfo.requestedPermissions?.contains(ACCESS_MOCK_LOCATION_PERMISSION) == true
            val isNotSelf = packageName != context.packageName

            (hasMockLocationPackage || hasMockPermission) && isNotSelf
        }.map(PackageInfo::packageName)
    }

    private fun PackageManager.getInstalledPackages(): List<PackageInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            getInstalledPackages(0)
        }
    }

    private companion object {

        const val ACCESS_MOCK_LOCATION_PERMISSION = "android.permission.ACCESS_MOCK_LOCATION"
    }
}