plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.compose.organism.events.calendar"
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:molecules"))
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.4.2")
}