plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.compose.theme"
}

dependencies {
    api("androidx.compose.material:material:1.1.1")
    api("com.google.accompanist:accompanist-insets:0.24.13-rc")
}