plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.deletion"
}

dependencies {
    api(project(":logic:habit-deleter"))
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}
