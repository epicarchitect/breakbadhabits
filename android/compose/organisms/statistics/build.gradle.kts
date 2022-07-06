plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.compose.organism.statistics"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:molecules"))
}