plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    commonMainApi(libs.adrielcafe.voyager.navigator)
//    commonMainApi(projects.multiplatform.ui.icons)
    commonMainApi(projects.multiplatform.database)
//    commonMainApi(projects.multiplatform.di.holder)
    commonMainApi(projects.multiplatform.foundation.uikit)
    commonMainImplementation(compose.materialIconsExtended)
//    commonMainApi(projects.multiplatform.presentation.dashboard)
//    commonMainApi(projects.multiplatform.presentation.habits)
//    commonMainApi(projects.multiplatform.presentation.app)
//    commonMainApi(projects.multiplatform.di.declaration)
}