plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.compose.organism.icons.selection"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:molecules"))
}