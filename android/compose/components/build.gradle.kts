plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.component"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.4.2")
}