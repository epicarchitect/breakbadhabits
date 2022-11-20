plugins {
    id("breakbadhabits.android.library")
}

android {
    namespace = "breakbadhabits.feature.habits"
}

dependencies {
    api(project(":entities"))
    api(project(":extensions:coroutines"))
    api("com.github.epicarchitect:validation:1.0.0")
}
