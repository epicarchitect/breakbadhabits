plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.compose)
}

dependencies {
    commonMainApi(libs.adrielcafe.voyager.navigator)
    commonMainApi(projects.multiplatform.di.holder)
    commonMainApi(projects.multiplatform.foundation.uikit)
    commonMainApi(projects.multiplatform.presentation.dashboard)
    commonMainApi(projects.multiplatform.presentation.habits)
}
