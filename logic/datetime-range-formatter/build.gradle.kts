plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.habit.abstinence.provider"
}

dependencies {
    api(project(":extensions:datetime"))
}