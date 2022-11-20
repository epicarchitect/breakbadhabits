plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "breakbadhabits.app.ui"
}

dependencies {
    api(project(":app-dependencies"))
    api(project(":ui-kit"))
}