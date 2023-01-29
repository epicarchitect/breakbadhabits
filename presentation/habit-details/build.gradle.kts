plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.details"
}

dependencies {
    api(project(":extensions:viewmodel"))
    api(project(":logic:habit-provider"))
}
