plugins {
    id("breakbadhabits.android.library")
    id("breakbadhabits.android.compose")
}

android {
    namespace = "epicarchitect.epicstore.navigation.compose"
}

dependencies {
    api(project(":epic-store:compose"))
    api("androidx.navigation:navigation-compose:2.5.1")
}