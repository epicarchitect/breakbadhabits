plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.di.declaration)
}