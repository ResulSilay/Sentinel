package com.rs.kit.root.model

data class RootReport(
    val isRooted: Boolean,
    val foundBinaries: Boolean,
    val foundRootApps: Boolean,
    val isDebuggable: Boolean,
    val suspiciousProperties: Boolean,
    val isEmulator: Boolean,
    val foundInMounts: Boolean,
    val foundByRuntime: Boolean,
    val foundInAdvancedPaths: Boolean,
)