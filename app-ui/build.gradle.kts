plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

dependencies {
    api(project(":app-dependencies"))
    api(project(":ui-kit"))
}