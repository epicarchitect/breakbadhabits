plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.compose)
}

dependencies {
    commonMainApi(libs.voyager.navigator)
    commonMainApi(projects.di.holder)
    commonMainApi(projects.foundation.uikit)
    commonMainApi(projects.presentation.dashboard)
    commonMainApi(projects.presentation.habits)
}