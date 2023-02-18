plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.creation"
}

dependencies {
    api(projects.framework.controllers)
    api(projects.framework.viewmodel)
    api(projects.app.logic.habits.creator)
    api(projects.app.logic.habits.iconProvider)
}
