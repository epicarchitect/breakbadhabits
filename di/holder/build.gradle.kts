plugins {
    id("convention.multiplatform.library")
    id("convention.multiplatform.compose")
}

dependencies {
    commonMainApi(compose.runtime)
    commonMainApi(projects.di.declaration)
}