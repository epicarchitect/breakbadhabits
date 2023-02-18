plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.track.creation"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habits.trackCreator)
}
