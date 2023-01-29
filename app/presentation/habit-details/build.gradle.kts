plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.details"
}

dependencies {
    api(project(":framework:viewmodel"))
    api(project(":app:logic:habit-provider"))
}
