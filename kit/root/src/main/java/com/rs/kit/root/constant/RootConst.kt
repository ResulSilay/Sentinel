package com.rs.kit.root.constant

internal object RootConst {

    const val PROC_MOUNTS_PATH = "/proc/self/mounts"

    const val TEST_KEYS_TAG = "test-keys"

    val RUNTIME_COMMAND_SU = arrayOf("which", "su")

    val ROOT_BINARY_PATHS = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su",
        "/su/bin/su"
    )

    val ROOT_PACKAGES = arrayOf(
        "com.noshufou.android.su",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.thirdparty.superuser",
        "com.yellowes.su",
        "com.topjohnwu.magisk",
        "com.kingroot.kinguser",
        "com.zhiqupk.root.global"
    )

    val SUSPICIOUS_MOUNTS = arrayOf(
        "magisk",
        "core/img",
        "mirror",
        "history",
        "init.magisk"
    )

    val ADVANCED_ROOT_PATHS = arrayOf(
        "/data/adb/magisk",
        "/data/adb/ksu",
        "/data/adb/ap",
        "/data/adb/magisk.db"
    )

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
}