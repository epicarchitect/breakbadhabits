plugins {
    id("convention.android.library")
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.foundation.icons)
    api(projects.logic.datetime)
    implementation(projects.database)
}