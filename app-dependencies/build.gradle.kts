plugins {
    id("breakbadhabits.android.library")
}

android {
    namespace = "breakbadhabits.app.dependencies"
}

dependencies {
    api(project(":features:habits"))
}