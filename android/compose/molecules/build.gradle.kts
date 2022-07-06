plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.molecule"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation("androidx.compose.material:material-icons-core:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
}