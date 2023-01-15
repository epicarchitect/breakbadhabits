plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.track.creation"
}

dependencies {
    api(project(":logic:habit-track-creator"))
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}
