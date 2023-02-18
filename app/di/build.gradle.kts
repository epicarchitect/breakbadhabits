plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.di"
}

dependencies {
    api(projects.app.presentation.dashboard)
    api(projects.app.presentation.habits.creation)
    api(projects.app.presentation.habits.deletion)
    api(projects.app.presentation.habits.details)
    api(projects.app.presentation.habits.trackDetails)
    api(projects.app.presentation.habits.trackCreation)
    implementation(projects.app.database)
}