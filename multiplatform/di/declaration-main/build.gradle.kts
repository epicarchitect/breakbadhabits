plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.di.declaration)
    commonMainApi(projects.multiplatform.database)
    commonMainApi(projects.multiplatform.foundation.coroutines)
    commonMainApi(libs.russhwolf.settingsNoArg)
}
