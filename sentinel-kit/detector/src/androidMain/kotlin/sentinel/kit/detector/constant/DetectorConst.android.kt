package sentinel.kit.detector.constant

object DetectorConst {

    const val TEST_KEYS_TAG: String = "test-keys"

    val EMULATOR_PIPES: List<String> = listOf(
        "/dev/socket/qemud",
        "/dev/qemu_pipe"
    )

    val EMULATOR_PROPS: List<String> = listOf(
        "google_sdk",
        "emulator",
        "android_sdk",
        "genymotion",
        "goldfish",
        "vbox86",
        "sdk_gphone"
    )

    val HOOK_PACKAGES: List<String> = listOf(
        "de.robv.android.xposed",
        "com.topjohnwu.lsposed",
        "org.meowcat.edxposed.manager",
        "com.saurik.substrate",
        "com.devadvance.rootcloak",
        "com.devadvance.rootcloakplus"
    )
}