enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "breakbadhabits"

includeBuild("build-logic")

include(
    ":app:android",
    ":app:di",
    ":app:database",
    ":app:entities",

    ":app:logic:habits:creator",
    ":app:logic:habits:deleter",
    ":app:logic:habits:track-creator",
    ":app:logic:habits:provider",
    ":app:logic:habits:track-provider",
    ":app:logic:habits:icon-provider",
    ":app:logic:datetime:formatter",
    ":app:logic:datetime:provider",

    ":app:presentation:dashboard",
    ":app:presentation:habits:creation",
    ":app:presentation:habits:deletion",
    ":app:presentation:habits:track-creation",
    ":app:presentation:habits:details",
    ":app:presentation:habits:track-details",

    ":framework:controllers",
    ":framework:coroutines",
    ":framework:datetime",
    ":framework:uikit",
    ":framework:viewmodel",
)