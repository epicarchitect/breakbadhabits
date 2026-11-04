rootProject.name = "breakbadhabits"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        // RuStore Pay SDK: репозиторий ограничен группой ru.rustore.sdk и нужен только rustore-flavor.
        maven {
            url = uri("https://nexus-external.rustore.ru/repository/maven-rustore-exposed")
            content {
                includeGroup("ru.rustore.sdk")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(
    "android-app",
    "multiplatform",
)
