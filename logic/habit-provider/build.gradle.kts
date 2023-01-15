plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.provider"
}

dependencies {
    implementation(project(":database"))
    api(project(":entities"))
    api(project(":extensions:coroutines"))
}