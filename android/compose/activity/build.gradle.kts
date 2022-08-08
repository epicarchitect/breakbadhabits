plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.activity"
}

dependencies {
    implementation(project(":android:compose:ui"))
    api("androidx.activity:activity-compose:1.5.1")
    api("androidx.appcompat:appcompat:1.4.2")
}