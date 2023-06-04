plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.ui.format)
    commonMainApi(projects.presentation.dashboard)
    commonMainApi(projects.presentation.habits)
    commonMainApi(projects.foundation.identification)
}