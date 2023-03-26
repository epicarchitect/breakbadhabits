enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

includeBuild("build-logic")

include(
    ":android-app",
    ":database",

    ":logic:habits",
    ":logic:datetime",
    ":logic:icons",

    ":presentation:dashboard",
    ":presentation:habits",

    ":foundation:controllers",
    ":foundation:coroutines",
    ":foundation:datetime",
    ":foundation:uikit",
    ":foundation:viewmodel",
    ":foundation:math",
)