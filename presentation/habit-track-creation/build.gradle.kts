plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.track.creation"
}

dependencies {
    api(project(":extensions:viewmodel"))
    api(project(":logic:habit-track-creator"))
}
