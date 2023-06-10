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
    ":android:app",
    ":android:habits-widget",

    ":multiplatform:app",
    ":multiplatform:database",

    ":multiplatform:ui:screens",
    ":multiplatform:ui:format",

    ":multiplatform:di:holder",
    ":multiplatform:di:declaration-main",
    ":multiplatform:di:declaration",

    ":multiplatform:logic:habits",
    ":multiplatform:logic:datetime",

    ":multiplatform:presentation:dashboard",
    ":multiplatform:presentation:habits",

    ":multiplatform:foundation:controllers",
    ":multiplatform:foundation:coroutines",
    ":multiplatform:foundation:datetime",
    ":multiplatform:foundation:uikit",
    ":multiplatform:foundation:viewmodel",
    ":multiplatform:foundation:math",
    ":multiplatform:foundation:icons",
    ":multiplatform:foundation:identification",
    ":multiplatform:foundation:settings",
)