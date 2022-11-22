rootProject.name = "Break Bad Habits"

include(
    ":app-dependencies",
    ":app-dependencies-main",
    ":extensions:coroutines",
    ":extensions:datetime",
    ":entities",
    ":features:habits",
    ":android-app",
    ":app-ui",
    ":ui-kit",

    ":epic-store:core",
    ":epic-store:compose",
    ":epic-store:google-navigation-compose",
)