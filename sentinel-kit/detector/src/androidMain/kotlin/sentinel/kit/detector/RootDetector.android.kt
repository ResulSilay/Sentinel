package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.SecurityViolation

actual class RootDetector actual constructor(
    private val context: Any?,
) : SecurityDetector {

    private val androidContext: Context
        get() = context as? Context
            ?: throw IllegalArgumentException("RootDetector requires a valid Android Context")

    init {
        System.loadLibrary("sentinel-root")
    }

    external fun checkApps(context: Context): Boolean

    external fun checkBinaries(): Boolean

    external fun checkMounts(): Boolean

    external fun checkSuCommand(): Boolean

    actual override fun detect(): List<Threat> = buildList {
        if (checkApps(context = androidContext)) {
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