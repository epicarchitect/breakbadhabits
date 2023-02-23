plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.foundation.controller"
}

dependencies {
    api(projects.foundation.coroutines)
}