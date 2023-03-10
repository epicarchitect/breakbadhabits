enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

includeBuild("build-logic")

include(
    ":android-app",
    ":di",
    ":database",
    ":entities",

    ":logic:habits",
    ":logic:datetime",

    ":presentation:dashboard",
    ":presentation:habits",

    ":foundation:controllers",
    ":foundation:coroutines",
    ":foundation:datetime",
    ":foundation:uikit",
    ":foundation:viewmodel",
)