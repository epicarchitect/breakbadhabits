plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.logic.habit.deleter"
}

dependencies {
    api(project(":framework:coroutines"))
    api(project(":app:entities"))
    implementation(project(":app:database"))
}