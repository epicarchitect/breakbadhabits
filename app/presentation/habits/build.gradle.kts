plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habits"
}

dependencies {
    api(projects.framework.controllers)
    api(projects.framework.viewmodel)
    api(projects.app.logic.habits)
}
