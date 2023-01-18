plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.logic.time.provider"
}

dependencies {
    api(project(":extensions:coroutines"))
    api(project(":extensions:datetime"))
}