pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "sentinel"

include(":sentinel")
include(":sentinel-ui")
include(":sentinel-core")
include(":sentinel-identity")
include(":sentinel-kit")
include(":sentinel-kit:ndk")
include(":sentinel-kit:detector")

include(":sample")
include(":sample:multiplatform")
include(":sample:multiplatform:composeApp")
