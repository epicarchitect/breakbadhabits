plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habits"
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.entities)
    api(projects.logic.datetime)
    implementation(projects.database)
}