rootProject.name = "breakbadhabits"

include(
    ":extensions:coroutines",
    ":extensions:datetime",

    ":entities",
    ":database",

    ":logic:habit-creator",
    ":logic:habit-deleter",
    ":logic:habit-track-creator",
    ":logic:habit-provider",
    ":logic:habit-track-provider",
    ":logic:habit-icons-provider",
    ":logic:habit-abstinence-provider",
    ":logic:time-provider",

    ":presentation:habit-creation",
    ":presentation:habit-deletion",
    ":presentation:habit-track-creation",
    ":presentation:habits-dashboard",
    ":presentation:habit-details",
    ":presentation:habit-track",

    ":ui-kit",
    ":android-app",
)