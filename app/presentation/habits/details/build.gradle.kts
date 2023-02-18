plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.details"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habits.provider)
}
