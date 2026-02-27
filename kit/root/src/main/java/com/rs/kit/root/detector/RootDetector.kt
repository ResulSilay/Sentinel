package com.rs.kit.root.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.type.SecurityType

class RootDetector(
    private val context: Context,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-root")
    }

    external fun checkApps(context: Context): Boolean

    external fun checkBinaries(): Boolean

    external fun checkMounts(): Boolean

    external fun checkSuCommand(): Boolean

    override fun detect(): Threat? = when {
        checkApps(context = context) -> Threat(
            type = SecurityType.ROOT,
            description = "Root apps installed",
            severity = 40
        )

        checkBinaries() -> Threat(
            type = SecurityType.ROOT,
            description = "Root binaries found",
            severity = 50
        )

        checkMounts() -> Threat(
            type = SecurityType.ROOT,
            description = "Suspicious mount points",
            severity = 45
        )

        checkSuCommand() -> Threat(
            type = SecurityType.ROOT,
            description = "SU command executed",
            severity = 60
        )

        else -> null
    }
}

