plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "epicarchitect.epicstore.compose"
}

dependencies {
    api(project(":epic-store:core"))
    api("androidx.compose.foundation:foundation:1.2.1")
}