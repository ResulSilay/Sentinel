package com.rs.kit.root.core

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug.isDebuggerConnected
import com.rs.kit.root.constant.RootConst
import com.rs.kit.root.model.RootReport
import java.io.File

class RootImpl(
    private val context: Context,
) : Root {

    private val securityChecks: List<() -> Boolean> = listOf(
        { checkBinaries() },
        { checkApps() },
        { checkSystemProperties() },
        { isDebuggable() },
        { checkEmulator() },
        { checkMountPoints() },
        { checkRuntimeCommands() },
        { checkAdvancedPaths() }
    )

    override fun isRooted(): Boolean = securityChecks.any { f -> f() }

    override fun getReport(): RootReport {
        val binaries = checkBinaries()
        val apps = checkApps()
        val props = checkSystemProperties()
        val debug = isDebuggable()
        val emulator = checkEmulator()
        val mounts = checkMountPoints()
        val runtime = checkRuntimeCommands()
        val advanced = checkAdvancedPaths()

        val isRooted = listOf(
            binaries,
            apps,
            props,
            debug,
            emulator,
            mounts,
            runtime,
            advanced
        ).any { r -> r }

        return RootReport(
            isRooted = isRooted,
            foundBinaries = binaries,
            foundRootApps = apps,
            isDebuggable = debug,
            suspiciousProperties = props,
            isEmulator = emulator,
            foundInMounts = mounts,
            foundByRuntime = runtime,
            foundInAdvancedPaths = advanced
        )
    }

    private fun checkBinaries(): Boolean = RootConst.ROOT_BINARY_PATHS.any { path ->
        File(path).exists()
    }

    private fun checkApps(): Boolean = RootConst.ROOT_PACKAGES.any { packageName ->
        runCatching {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        }.getOrElse {
            false
        }
    }

    private fun checkSystemProperties(): Boolean {
        val tags = Build.TAGS
        return tags != null && tags.contains(RootConst.TEST_KEYS_TAG)
    }

    private fun checkMountPoints(): Boolean = runCatching {
        File(RootConst.PROC_MOUNTS_PATH).useLines { lines ->
            lines.any { line ->
                RootConst.SUSPICIOUS_MOUNTS.any { suspicious ->
                    line.contains(other = suspicious, ignoreCase = true)
                }
            }
        }
    }.getOrDefault(false)

    private fun checkRuntimeCommands(): Boolean {
        var process: Process? = null
        return runCatching {
            process = Runtime.getRuntime().exec(RootConst.RUNTIME_COMMAND_SU)
            process?.inputStream?.bufferedReader()?.use { reader ->
                reader.readLine() != null
            } ?: false
        }.also {
            process?.destroy()
        }.getOrDefault(false)
    }

    private fun checkAdvancedPaths(): Boolean =
        RootConst.ADVANCED_ROOT_PATHS.any { p -> File(p).exists() }

    private fun isDebuggable(): Boolean {
        val isDebuggerConnected = isDebuggerConnected()
        val isAppDebuggable =
            (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        return isDebuggerConnected || isAppDebuggable
    }

    private fun checkEmulator(): Boolean {
        val buildDetails = (Build.FINGERPRINT + Build.DEVICE + Build.MODEL +
                Build.BRAND + Build.PRODUCT + Build.MANUFACTURER + Build.HARDWARE).lowercase()
        val propCheck = RootConst.EMULATOR_PROPS.any { buildDetails.contains(it) }
        val pipeCheck = RootConst.EMULATOR_PIPES.any { File(it).exists() }
        return propCheck || pipeCheck
    }
}