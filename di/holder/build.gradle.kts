plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.compose)
}

dependencies {
    commonMainApi(compose.runtime)
    commonMainApi(projects.di.declaration)
}