plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":entities"))
    api(project(":extensions:coroutines"))
    api("com.github.epicarchitect:validation:1.0.0")
}
