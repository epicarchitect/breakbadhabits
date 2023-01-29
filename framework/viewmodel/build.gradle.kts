plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.framework.viewmodel"
}

dependencies {
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
}