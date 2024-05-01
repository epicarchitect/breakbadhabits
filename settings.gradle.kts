enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

pluginManagement {
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

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(
    ":android-app",
    ":kmp-lib"
)