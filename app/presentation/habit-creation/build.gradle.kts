plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.creation"
}

dependencies {
    api(project(":framework:viewmodel"))
    api(project(":app:logic:habit-creator"))
    api(project(":app:logic:habit-icon-provider"))
}
