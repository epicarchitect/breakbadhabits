plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.framework.controller"
}

dependencies {
    api(projects.framework.coroutines)
}