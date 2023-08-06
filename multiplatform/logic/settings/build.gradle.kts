plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.coroutines)
    commonMainImplementation(projects.multiplatform.database)
}