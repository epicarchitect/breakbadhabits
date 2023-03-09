plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habits"
}

dependencies {
    api(projects.foundation.controllers)
    api(projects.foundation.viewmodel)
    api(projects.logic.habits)
}
