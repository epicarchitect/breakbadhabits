plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.coroutines)
    commonMainApi(projects.multiplatform.foundation.datetime)
}