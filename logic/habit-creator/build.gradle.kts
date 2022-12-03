plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":entities"))
    api(project(":extensions:coroutines"))
}