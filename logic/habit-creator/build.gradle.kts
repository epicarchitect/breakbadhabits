plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.creator"
}

dependencies {
    api(project(":extensions:coroutines"))
    api(project(":entities"))
    implementation(project(":database"))
}