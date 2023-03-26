plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habits"
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.logic.datetime)
    api(projects.logic.icons)
    implementation(projects.database)
}