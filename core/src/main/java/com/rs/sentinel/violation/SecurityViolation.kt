package com.rs.sentinel.violation

sealed class SecurityViolation(val severity: Int) {

    sealed class Root(severity: Int) : SecurityViolation(severity) {

        object SuBinaryFound : Root(60)

        object SuCommandExecuted : Root(70)

        data class SuspiciousMount(val mountPoint: String? = null) : Root(45)

        data class RootAppInstalled(val packageName: String? = null) : Root(40)
    }

    sealed class Tamper(severity: Int) : SecurityViolation(severity) {

        object PackageNameChanged : Tamper(80)

        object DexIntegrityFailed : Tamper(90)

        object SignatureMismatch : Tamper(100)
    }

    sealed class Hook(severity: Int) : SecurityViolation(severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 90)

        object FridaDetected : Root(95)
    }

    sealed class Emulator(severity: Int) : SecurityViolation(severity) {

        data class Detected(val name: String? = null) : Emulator(30)
    }

    sealed class Debugger(severity: Int) : SecurityViolation(severity) {

        object Debuggable : Emulator(30)

        object TestKeys : Emulator(30)
    }

    sealed class Location(severity: Int) : SecurityViolation(severity) {

        object MockSettingEnabled : Location(60)

        object MockLocationDetected : Location(80)

        data class MockAppInstalled(val packages: List<String>) : Location(40)
    }
}