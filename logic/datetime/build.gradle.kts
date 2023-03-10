plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.datetime"
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.foundation.datetime)
}