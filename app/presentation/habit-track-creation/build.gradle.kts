plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.track.creation"
}

dependencies {
    api(project(":framework:viewmodel"))
    api(project(":app:logic:habit-track-creator"))
}
