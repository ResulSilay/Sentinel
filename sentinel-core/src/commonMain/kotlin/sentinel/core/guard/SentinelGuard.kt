package sentinel.core.guard

import sentinel.core.report.SecurityReport

class SentinelGuard(val report: SecurityReport) {

    inline fun onCompromised(action: () -> Unit) {
        if (report.isRooted || report.isJailbroken) action()
    }

    inline fun onTampered(action: () -> Unit) {
        if (report.isTampered) action()
    }

    inline fun onHooked(action: () -> Unit) {
        if (report.isHooked) action()
    }

    inline fun onSimulated(action: () -> Unit) {
        if (report.isSimulator) action()
        if (report.isEmulator) action()
    }

    inline fun onDebugged(action: () -> Unit) {
        if (report.isDebugged) action()
    }

    inline fun onCritical(action: (score: Int) -> Unit) {
        if (report.isCritical()) action(report.severity)
    }

    inline fun onSafe(action: () -> Unit) {
        if (report.isSafe()) action()
    }
}