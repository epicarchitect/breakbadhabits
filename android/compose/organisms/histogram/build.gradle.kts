plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.organism.histogram"
}

dependencies {
    implementation(project(":android:compose:molecules"))
    implementation("androidx.compose.material:material:1.1.1")
}