package sentinel.kit.detector.constant

object DetectorConst {

    val JAILBREAK_APPS: List<String> = listOf(
        "/Applications/Cydia.app",
        "/Library/MobileSubstrate/MobileSubstrate.dylib",
        "/bin/bash",
        "/usr/sbin/sshd",
        "/etc/apt",
        "/private/var/lib/apt/",
        "/Applications/Sileo.app",
        "/Applications/Zebra.app",
        "/Applications/Filza.app",
        "/var/lib/cydia"
    )

    val MOUNTS: List<String> = listOf(
        "/Library",
        "/usr/lib",
        "/bin",
        "/etc",
        "/var"
    )

    val SIMULATOR_KEYS: List<String> = listOf(
        "SIMULATOR_DEVICE_NAME",
        "SIMULATOR_MODEL_IDENTIFIER"
    )

    val SIMULATOR_MODEL_KEYS: List<String> = listOf(
        "Simulator"
    )
}