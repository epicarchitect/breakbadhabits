plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.foundation.coroutines)
    commonMainApi(projects.foundation.datetime)
}