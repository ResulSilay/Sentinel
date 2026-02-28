package com.rs.kit.root.detector

import android.content.Context
import com.rs.sentinel.detector.SecurityDetector
import com.rs.sentinel.detector.Threat
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

    override fun detect(): List<Threat> = buildList {
        if (checkApps(context = context)) {
            add(
                Threat(
                    violation = SecurityViolation.Root.RootAppInstalled()
                )
            )
        }

        if (checkBinaries()) {
            add(
                Threat(
                    violation = SecurityViolation.Root.SuBinaryFound
                )
            )
        }

        if (checkMounts()) {
            add(
                Threat(
                    violation = SecurityViolation.Root.SuspiciousMount()
                )
            )
        }

        if (checkSuCommand()) {
            add(
                Threat(
                    violation = SecurityViolation.Root.SuCommandExecuted
                )
            )
        }
    }
}

