plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.track.details"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habits.trackProvider)
}