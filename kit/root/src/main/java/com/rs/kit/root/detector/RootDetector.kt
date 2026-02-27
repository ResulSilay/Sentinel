package com.rs.kit.root.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.model.Threat
import com.rs.sentinel.violation.SecurityViolation

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
            violation = SecurityViolation.Root.RootAppInstalled()
        )

        checkBinaries() -> Threat(
            violation = SecurityViolation.Root.SuBinaryFound,
        )

        checkMounts() -> Threat(
            violation = SecurityViolation.Root.SuspiciousMount(),
        )

        checkSuCommand() -> Threat(
            violation = SecurityViolation.Root.SuCommandExecuted,
        )

        else -> null
    }
}

