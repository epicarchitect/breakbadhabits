plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.creation"
}

dependencies {
    api(project(":extensions:viewmodel"))
    api(project(":logic:habit-creator"))
    api(project(":logic:habit-icons-provider"))
}
