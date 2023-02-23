enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

includeBuild("build-logic")

include(
    ":app:android",
    ":app:di",
    ":app:database",
    ":app:entities",

    ":app:logic:habits",
    ":app:logic:datetime:formatter",
    ":app:logic:datetime:provider",

    ":app:presentation:dashboard",
    ":app:presentation:habits",

    ":foundation:controllers",
    ":foundation:coroutines",
    ":foundation:datetime",
    ":foundation:uikit",
    ":foundation:viewmodel",
)