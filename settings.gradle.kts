enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
}

include(
    ":android-app",
    ":shared",
    ":database",
    ":habits-widget-android",

    ":ui:screens",
    ":ui:format",

    ":di:holder",
    ":di:declaration-main",
    ":di:declaration",

    ":logic:habits",
    ":logic:datetime",

    ":presentation:dashboard",
    ":presentation:habits",

    ":foundation:controllers",
    ":foundation:coroutines",
    ":foundation:datetime",
    ":foundation:uikit",
    ":foundation:viewmodel",
    ":foundation:math",
    ":foundation:icons",
    ":foundation:identification",
    ":foundation:settings",
)