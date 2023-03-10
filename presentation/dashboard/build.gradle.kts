plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.dashboard"
}

dependencies {
    api(projects.foundation.controllers)
    api(projects.foundation.viewmodel)
    api(projects.logic.habits)
    api(projects.logic.datetime)
}
