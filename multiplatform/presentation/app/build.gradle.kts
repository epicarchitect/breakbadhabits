plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.viewmodel)
    commonMainApi(projects.multiplatform.di.declaration)
//    commonMainApi(projects.multiplatform.presentation.dashboard)
//    commonMainApi(projects.multiplatform.presentation.habits)
//    commonMainApi(projects.multiplatform.presentation.settings)
}