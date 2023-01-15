plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.deleter"
}

dependencies {
    implementation(project(":database"))
    api(project(":entities"))
    api(project(":extensions:coroutines"))
}