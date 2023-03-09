plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habits"
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.entities)
    implementation(projects.database)
}