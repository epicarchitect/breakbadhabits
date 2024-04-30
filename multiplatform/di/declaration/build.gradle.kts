plugins {
    id("convention.multiplatform.library")
}

dependencies {
    commonMainApi(projects.multiplatform.ui.format)
//    commonMainApi(projects.multiplatform.logic.habits)
    commonMainApi(projects.multiplatform.database)
//    commonMainApi(projects.multiplatform.logic.datetime)
//    commonMainApi(projects.multiplatform.logic.settings)
//    commonMainApi(projects.multiplatform.foundation.identification)
}