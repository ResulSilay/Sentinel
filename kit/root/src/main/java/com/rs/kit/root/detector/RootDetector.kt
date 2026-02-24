package com.rs.kit.root.detector

import android.content.Context
import com.rs.sentinel.constant.SentinelConst
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType
import java.io.File

class RootDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): Threat? = when {
        checkBinaries() -> Threat(
            type = SecurityType.ROOT,
            description = "Root binaries found",
            severity = 50
        )

        checkApps() -> Threat(
            type = SecurityType.ROOT,
            description = "Root apps installed",
            severity = 40
        )

        checkMountPoints() -> Threat(
            type = SecurityType.ROOT,
            description = "Suspicious mount points",
            severity = 45
        )

        checkRuntime() -> Threat(
            type = SecurityType.ROOT,
            description = "SU command executed",
            severity = 60
        )

        else -> null
    }

    private fun checkBinaries() =
        (SentinelConst.ROOT_BINARY_PATHS + SentinelConst.ADVANCED_ROOT_PATHS).any { pathName ->
            File(pathName).exists()
        }

    private fun checkApps() = SentinelConst.ROOT_PACKAGES.any { pkg ->
        runCatching {
            context.packageManager.getPackageInfo(pkg, 0); true
        }.getOrDefault(false)
    }

    private fun checkMountPoints() = runCatching {
        File(SentinelConst.PROC_MOUNTS_PATH).useLines { lines ->
            lines.any { line ->
                SentinelConst.SUSPICIOUS_MOUNTS.any { suspiciousMount ->
                    line.contains(
                        other = suspiciousMount,
                        ignoreCase = true
                    )
                }
            }
        }
    }.getOrDefault(false)

    private fun checkRuntime() = runCatching {
        val process = Runtime.getRuntime().exec(SentinelConst.COMMAND_SU)
        val hasOutput = process.inputStream.bufferedReader().use { it.readLine() != null }
        process.destroy()
        hasOutput
    }.getOrDefault(false)
}

