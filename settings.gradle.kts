rootProject.name = "breakbadhabits"

include(
    ":app-dependencies",
    ":app-dependencies-main",
    ":extensions:coroutines",
    ":extensions:datetime",
    ":entities",

    ":logic:core",
    ":logic:habit-creator",
    ":logic:habit-ids-provider",
    ":logic:habit-icons-provider",
    ":logic:current-habit-abstinence-provider",

    ":logic:habit-new-name-validator",
    ":logic:habit-track-interval-validator",

//    ":features:habits",
    ":presentation:core",
    ":presentation:habit-creation",
    ":presentation:current-habit-abstinence",
    ":presentation:habit-abstinence-formatter",
    ":presentation:habit-track-interval-formatter",

    ":android-app",
    ":app-ui",
    ":ui-kit",

    ":epic-store:core",
    ":epic-store:compose",
    ":epic-store:google-navigation-compose",
)