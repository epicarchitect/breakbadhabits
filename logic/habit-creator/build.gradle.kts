plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.creator"
}

dependencies {
    implementation(project(":database"))
    api(project(":entities"))
    api(project(":extensions:coroutines"))
    api(project(":extensions:datetime"))
}