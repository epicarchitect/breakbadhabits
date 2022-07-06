plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.organism.histogram"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:molecules"))
}