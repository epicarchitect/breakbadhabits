plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.dashboard"
}

dependencies {
    api(projects.framework.viewmodel)
    api(projects.app.logic.habitProvider)
    api(projects.app.logic.habitTrackProvider)
    api(projects.app.logic.datetimeFormatter)
    api(projects.app.logic.datetimeProvider)
}
