plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habits.dashboard"
}

dependencies {
    api(project(":logic:habit-provider"))
    api(project(":logic:habit-abstinence-provider"))
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}
