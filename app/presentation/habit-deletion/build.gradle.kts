plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.habit.deletion"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habitDeleter)
}
