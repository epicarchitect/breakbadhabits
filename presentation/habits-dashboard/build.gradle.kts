plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habits.dashboard"
}

dependencies {
    api(project(":extensions:viewmodel"))
    api(project(":logic:habit-provider"))
    api(project(":logic:habit-track-provider"))
    api(project(":logic:datetime-range-formatter"))
    api(project(":logic:datetime-provider"))
}
