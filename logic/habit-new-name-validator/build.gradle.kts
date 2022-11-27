plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":logic:core"))
    api("com.github.epicarchitect:validation:1.0.0")
}