plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.di"
}

dependencies {
    api(projects.app.presentation.dashboard)
    api(projects.app.presentation.habitCreation)
    api(projects.app.presentation.habitDeletion)
    api(projects.app.presentation.habitDetails)
    api(projects.app.presentation.habitTrackDetails)
    api(projects.app.presentation.habitTrackCreation)
    implementation(projects.app.database)
}