plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.foundation.math)
    commonMainApi(libs.kotlin.datetime)
}