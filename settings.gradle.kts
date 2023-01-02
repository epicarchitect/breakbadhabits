rootProject.name = "breakbadhabits"

include(
//    ":app-dependencies",
//    ":app-dependencies-main",
    ":extensions:coroutines",
    ":extensions:datetime",
    ":entities",

    ":logic:habit-creator",
    ":logic:habit-deleter",
    ":logic:habit-track-creator",
    ":logic:habit-ids-provider",
    ":logic:habit-provider",
    ":logic:habit-track-provider",
    ":logic:habit-icons-provider",
    ":logic:current-habit-abstinence-provider",

    ":presentation:core",
    ":presentation:habit-creation",
    ":presentation:habit-deletion",
    ":presentation:habit-track-creation",
    ":presentation:current-habit-abstinence",
    ":presentation:habit-ids",
    ":presentation:habit",
    ":presentation:habit-track",

    ":android-app",
//    ":app-ui",
    ":ui-kit",

    ":epic-store:core",
    ":epic-store:compose",
    ":epic-store:google-navigation-compose",
)