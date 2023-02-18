plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.datetime.provider"
}

dependencies {
    api(projects.framework.coroutines)
    api(projects.framework.datetime)
}