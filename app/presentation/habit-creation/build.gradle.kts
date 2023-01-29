plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.creation"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habitCreator)
    api(projects.app.logic.habitIconProvider)
}
