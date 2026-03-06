package sentinel.core.report

import sentinel.core.type.RiskLevel
import sentinel.core.violation.SecurityViolation
import sentinel.core.detector.Threat
import kotlin.reflect.KClass

data class SecurityReport(
    val threats: List<Threat>,
    val threshold: Int,
    val timestamp: Long = 0,
) {
    val severity: Int by lazy {
        threats.sumOf { threat -> threat.violation.severity }
    }

    val riskLevel: RiskLevel by lazy {
        RiskLevel.Companion.getLevel(
            severity = severity,
            threshold = threshold
        )
    }

    val isRooted: Boolean
        get() = hasViolationCategory(SecurityViolation.Root::class)

    val isTampered: Boolean
        get() = hasViolationCategory(SecurityViolation.Tamper::class)

    val isHooked: Boolean
        get() = hasViolationCategory(SecurityViolation.Hook::class)

    val isEmulator: Boolean
        get() = hasViolationCategory(SecurityViolation.Emulator::class)

    val isDebuggable: Boolean
        get() = hasViolationCategory(SecurityViolation.Debugger::class)

    val isMockLocation: Boolean
        get() = hasViolationCategory(SecurityViolation.Location::class)

    fun isSafe(): Boolean = riskLevel == RiskLevel.SAFE

    fun isCritical(): Boolean = riskLevel == RiskLevel.HIGH

    private fun hasViolationCategory(category: KClass<out SecurityViolation>): Boolean {
        return threats.any { threat -> category.isInstance(threat.violation) }
    }
}