plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.rs.sentinel"
    compileSdk = Config.Version.COMPILE_SDK

    resourcePrefix = "sentinel_"

    defaultConfig {
        minSdk = Config.Version.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = Config.IS_MINIFY_ENABLED
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        jvmToolchain(jdkVersion = 21)
    }

    publishing {
        singleVariant(variantName = "release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>(name = "release") {
                groupId = Config.GROUP_ID
                artifactId = Config.ARTIFACT_ID
                version = Config.Version.NAME

                afterEvaluate {
                    from(components["release"])
                }
            }
        }

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/ResulSilay/Sentinel")
                credentials {
                    username = System.getenv("GITHUB_USER")
                    password = System.getenv("GITHUB_PWD")
                }
            }
        }
    }
}

dependencies {
    api(project(":core"))
    api(project(":kit:root"))
    api(project(":kit:tamper"))
    api(project(":kit:emulator"))
    api(project(":kit:debug"))
    api(project(":kit:hook"))
    api(project(":kit:location"))
}