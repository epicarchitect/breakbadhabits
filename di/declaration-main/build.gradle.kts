plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.di.declaration)
    commonMainApi(projects.database)
    commonMainApi(projects.foundation.coroutines)
    commonMainApi(libs.russhwolf.settingsNoArg)
}