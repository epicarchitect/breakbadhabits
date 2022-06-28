plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.compose.organism.events.calendar"
}

dependencies {
    implementation(project(":android:compose:molecules"))
    implementation("androidx.compose.material:material:1.1.1")
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.4.2")
}