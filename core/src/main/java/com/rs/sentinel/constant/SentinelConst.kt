package com.rs.sentinel.constant

object SentinelConst {

    const val TEST_KEYS_TAG = "test-keys"

    val EMULATOR_PIPES = arrayOf(
        "/dev/socket/qemud",
        "/dev/qemu_pipe"
    )

    val EMULATOR_PROPS = arrayOf(
        "google_sdk",
        "emulator",
        "android_sdk",
        "genymotion",
        "goldfish",
        "vbox86",
        "sdk_gphone"
    )

    val HOOK_PACKAGES = arrayOf(
        "de.robv.android.xposed",
        "com.topjohnwu.lsposed",
        "org.meowcat.edxposed.manager",
        "com.saurik.substrate",
        "com.devadvance.rootcloak",
        "com.devadvance.rootcloakplus"
    )
}