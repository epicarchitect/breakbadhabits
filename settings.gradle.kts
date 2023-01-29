enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

includeBuild("build-logic")

include(
    ":app:android",
    ":app:di",
    ":app:database",
    ":app:entities",

    ":app:logic:habit-creator",
    ":app:logic:habit-deleter",
    ":app:logic:habit-track-creator",
    ":app:logic:habit-provider",
    ":app:logic:habit-track-provider",
    ":app:logic:habit-icon-provider",
    ":app:logic:datetime-formatter",
    ":app:logic:datetime-provider",

    ":app:presentation:dashboard",
    ":app:presentation:habit-creation",
    ":app:presentation:habit-deletion",
    ":app:presentation:habit-track-creation",
    ":app:presentation:habit-details",
    ":app:presentation:habit-track-details",

    ":framework:uikit",
    ":framework:coroutines",
    ":framework:datetime",
    ":framework:viewmodel",
)