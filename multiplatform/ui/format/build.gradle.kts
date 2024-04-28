plugins {
    id("convention.multiplatform.library")
}

dependencies {
//    commonMainApi(projects.multiplatform.logic.datetime)
    commonMainApi(projects.multiplatform.foundation.datetime)
}