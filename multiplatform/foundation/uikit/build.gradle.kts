plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    sourceSets {
        getByName("androidMain") {
            dependencies {
                api(libs.android.coreKtx)
                api(libs.android.activityCompose)
            }
        }
    }
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.controllers)
    commonMainApi(projects.multiplatform.foundation.datetime)
    commonMainApi(compose.foundation)
    commonMainImplementation(compose.material3)
    commonMainImplementation(libs.epicarchitect.calendarComposeDatePicker)
}