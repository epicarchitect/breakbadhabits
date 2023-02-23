plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.di"
}

dependencies {
    api(projects.app.presentation.dashboard)
    api(projects.app.presentation.habits)
    implementation(projects.app.database)
}