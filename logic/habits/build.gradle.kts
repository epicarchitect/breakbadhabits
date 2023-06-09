plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.foundation.coroutines)
    commonMainApi(projects.foundation.icons)
    commonMainApi(projects.foundation.identification)
    commonMainApi(projects.foundation.math)
    commonMainApi(projects.logic.datetime)
    commonMainImplementation(projects.database)
}