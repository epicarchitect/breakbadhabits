import breakbadhabits.config.android.library.composeLibraryConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    composeLibraryConfig()
    namespace = "breakbadhabits.compose.organism.events.calendar"
}

dependencies {
    implementation(project(":android:compose:molecules"))
    implementation("androidx.compose.material:material:1.1.1")
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.4.2")
}