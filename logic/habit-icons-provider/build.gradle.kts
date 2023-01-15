plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.icons.provider"
}

dependencies {
    api(project(":entities"))
}