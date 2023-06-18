plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.coroutines)
    commonMainApi(projects.multiplatform.foundation.icons)
    commonMainApi(projects.multiplatform.foundation.identification)
    commonMainApi(projects.multiplatform.foundation.math)
    commonMainApi(projects.multiplatform.logic.datetime)
    commonMainImplementation(projects.multiplatform.database)
}
