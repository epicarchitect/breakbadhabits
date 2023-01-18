rootProject.name = "breakbadhabits"

include(
    ":android-app",
    ":ui-kit",
    ":app-dependencies",
    ":database",
    ":entities",

    ":logic:habit-creator",
    ":logic:habit-deleter",
    ":logic:habit-track-creator",
    ":logic:habit-provider",
    ":logic:habit-track-provider",
    ":logic:habit-icons-provider",
    ":logic:datetime-range-formatter",
    ":logic:datetime-provider",

    ":presentation:habit-creation",
    ":presentation:habit-deletion",
    ":presentation:habit-track-creation",
    ":presentation:habits-dashboard",
    ":presentation:habit-details",
    ":presentation:habit-track",

    ":extensions:coroutines",
    ":extensions:datetime",
)