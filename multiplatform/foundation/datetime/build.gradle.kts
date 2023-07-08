plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.math)
    commonMainApi(libs.jetbrains.kotlin.datetime)
}