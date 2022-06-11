import breakbadhabits.config.android.library.composeLibraryConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    composeLibraryConfig()
    namespace = "breakbadhabits.compose.theme"
}

dependencies {
    implementation("androidx.compose.material:material:1.1.1")
}