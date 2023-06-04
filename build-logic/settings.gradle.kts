rootProject.name = "build-logic"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}