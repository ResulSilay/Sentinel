plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
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