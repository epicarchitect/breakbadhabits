plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.cashapp.sqldelight)
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.sqldelight)
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.sqldelight.main")
        }
    }
}