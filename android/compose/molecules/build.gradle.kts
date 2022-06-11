import breakbadhabits.config.android.library.composeLibraryConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    composeLibraryConfig()
    namespace = "breakbadhabits.android.compose.molecule"
}

dependencies {
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.material:material-icons-core:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
}