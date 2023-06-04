plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.foundation.controllers)
    commonMainApi(projects.foundation.viewmodel)
    commonMainApi(projects.logic.habits)
    commonMainApi(projects.logic.datetime)
}
