plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.foundation.viewmodel"
}

dependencies {
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}