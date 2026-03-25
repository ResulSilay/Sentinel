plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.android.lint)
    id("sentinel-publish")
}

group = Config.Publishing.GROUP_ID
version = Config.Version.NAME

kotlin {

    android {
        namespace = "${Config.NAMESPACE}.kit.detector"

        compileSdk {
            version = release(36) { minorApiLevel = 1 }
        }

        minSdk = 24
    }

    val xcfName = "sentinel-kit-detector"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":sentinel-core"))
            }
        }

        androidMain {
            dependencies {
                api(project(":sentinel-kit:ndk"))
            }
        }
    }

    val iosTargets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())

    iosTargets.forEach { target ->
        target.compilations.getByName("main") {
            cinterops {
                val debuggerInterop by creating {
                    definitionFile.set(
                        project.file(
                            if (target.konanTarget.name.contains("sim"))
                                "src/nativeInterop/cinterop/debugger/debugger_sim.def"
                            else
                                "src/nativeInterop/cinterop/debugger/debugger_device.def"
                        )
                    )
                    includeDirs.allHeaders("src/nativeInterop/cinterop/debugger/")
                }
            }
        }
    }
}