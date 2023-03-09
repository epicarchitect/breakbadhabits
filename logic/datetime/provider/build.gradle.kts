plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.datetime.provider"
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.foundation.datetime)
}