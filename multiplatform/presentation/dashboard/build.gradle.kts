plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.controllers)
    commonMainApi(projects.multiplatform.foundation.viewmodel)
    commonMainApi(projects.multiplatform.logic.habits)
    commonMainApi(projects.multiplatform.logic.datetime)
}
