plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.ui.format)
    commonMainApi(projects.multiplatform.presentation.dashboard)
    commonMainApi(projects.multiplatform.presentation.habits)
    commonMainApi(projects.multiplatform.foundation.identification)
}