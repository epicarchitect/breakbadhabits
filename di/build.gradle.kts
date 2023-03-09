plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.di"
}

dependencies {
    api(projects.presentation.dashboard)
    api(projects.presentation.habits)
    implementation(projects.database)
}