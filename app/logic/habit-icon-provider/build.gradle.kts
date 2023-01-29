plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habit.icon.provider"
}

dependencies {
    api(project(":app:entities"))
}