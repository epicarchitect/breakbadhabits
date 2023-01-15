plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.abstinence.provider"
}

dependencies {
    implementation(project(":database"))
    api(project(":logic:time-provider"))
    api(project(":entities"))
    api(project(":extensions:coroutines"))
    api(project(":extensions:datetime"))
}