plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habit.creator"
}

dependencies {
    api(projects.framework.coroutines)
    api(projects.app.entities)
    implementation(projects.app.database)
}