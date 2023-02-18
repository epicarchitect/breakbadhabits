plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habit.track.provider"
}

dependencies {
    api(projects.framework.coroutines)
    api(projects.app.entities)
    implementation(projects.app.database)
}