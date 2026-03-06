package sentinel.kit.detector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug.isDebuggerConnected
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.SecurityViolation
import sentinel.kit.detector.constant.DetectorConst

actual class DebugDetector actual constructor(
    private val context: Any?,
) : SecurityDetector {

    private val androidContext: Context
        get() = context as? Context
            ?: throw IllegalArgumentException("RootDetector requires a valid Android Context")

    private val flags = androidContext.applicationInfo.flags

    actual override fun detect(): List<Threat> {
        val isDebugger = isDebuggerConnected()
        val isDebuggable = (flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        val isTestKeys = Build.TAGS?.contains(other = DetectorConst.TEST_KEYS_TAG) == true

        return buildList {
            if (isDebugger || isDebuggable) {
                add(
                    Threat(
                        violation = SecurityViolation.Debugger.Debuggable
                    )
                )
            }

            if (isTestKeys) {
                add(
                    Threat(
                        violation = SecurityViolation.Debugger.TestKeys
                    )
                )
            }
        }
    }
}