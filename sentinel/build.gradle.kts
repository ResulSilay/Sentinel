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
            isMinifyEnabled = false
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

    implementation(project(":kit:root"))
    implementation(project(":kit:tamper"))
    implementation(project(":kit:emulator"))
    implementation(project(":kit:debug"))
    implementation(project(":kit:hook"))
    implementation(project(":kit:location"))
}