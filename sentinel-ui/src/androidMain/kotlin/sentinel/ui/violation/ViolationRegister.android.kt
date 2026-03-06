package sentinel.ui.violation

import sentinel.core.violation.SecurityViolation

actual fun getViolations(): List<SecurityViolation> = listOf(
    SecurityViolation.Root.SuBinaryFound,
    SecurityViolation.Root.SuCommandExecuted,
    SecurityViolation.Root.SuspiciousMount(),
    SecurityViolation.Root.RootAppInstalled(),

    SecurityViolation.Tamper.PackageNameChanged,
    SecurityViolation.Tamper.DexIntegrityFailed,
    SecurityViolation.Tamper.SignatureMismatch,

    SecurityViolation.Hook.FrameworkDetected(),
    SecurityViolation.Hook.FridaDetected,

    SecurityViolation.Emulator.Detected(),
    SecurityViolation.Debugger.Debuggable,
    SecurityViolation.Debugger.TestKeys,
    SecurityViolation.Location.MockSettingEnabled,
    SecurityViolation.Location.MockLocationDetected,
    SecurityViolation.Location.MockAppInstalled(emptyList())
)

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is SecurityViolation.Root -> "Root"
    is SecurityViolation.Tamper -> "Tamper"
    is SecurityViolation.Hook -> "Hook"
    is SecurityViolation.Emulator -> "Emulator"
    is SecurityViolation.Debugger -> "Debugger"
    is SecurityViolation.Location -> "Location"
}