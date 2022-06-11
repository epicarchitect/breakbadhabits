import breakbadhabits.config.android.library.composeLibraryConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    composeLibraryConfig()
    namespace = "breakbadhabits.android.compose.organism.histogram"
}

dependencies {
    implementation(project(":android:compose:molecules"))
    implementation("androidx.compose.material:material:1.1.1")
}