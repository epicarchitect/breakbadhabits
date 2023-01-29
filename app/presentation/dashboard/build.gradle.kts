plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.presentation.dashboard"
}

dependencies {
    api(project(":framework:viewmodel"))
    api(project(":app:logic:habit-provider"))
    api(project(":app:logic:habit-track-provider"))
    api(project(":app:logic:datetime-formatter"))
    api(project(":app:logic:datetime-provider"))
}
