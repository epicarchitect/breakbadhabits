plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.creation"
}

dependencies {
    api(project(":logic:habit-creator"))
    api(project(":logic:habit-icons-provider"))
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}
