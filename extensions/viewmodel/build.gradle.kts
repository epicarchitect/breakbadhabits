plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.extension.viewmodel"
}

dependencies {
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}