plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.track"
}

dependencies {
    api(project(":logic:habit-track-provider"))
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}