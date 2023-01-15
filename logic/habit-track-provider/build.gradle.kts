plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.track.provider"
}

dependencies {
    implementation(project(":database"))
    api(project(":entities"))
    api(project(":extensions:coroutines"))
    api(project(":extensions:datetime"))
}