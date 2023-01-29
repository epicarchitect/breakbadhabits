plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.datetime.formatter"
}

dependencies {
    api(projects.framework.datetime)
}