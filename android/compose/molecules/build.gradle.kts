plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.molecule"
}

dependencies {
    implementation(project(":android:compose:theme"))
}