plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.datetime.provider"
}

dependencies {
    api(project(":framework:coroutines"))
    api(project(":framework:datetime"))
}