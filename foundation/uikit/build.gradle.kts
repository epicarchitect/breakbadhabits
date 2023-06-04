plugins {
    id("convention.multiplatform.library")
    id("convention.multiplatform.compose")
    id("convention.android.library")
}

kotlin {
    android()

    sourceSets {
        getByName("androidMain") {
            dependencies {
                api("androidx.core:core-ktx:1.10.1")
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.navigation:navigation-compose:2.5.3")
                implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
            }
        }
    }
}

dependencies {
    commonMainApi(projects.foundation.controllers)
    commonMainApi(projects.foundation.datetime)
    commonMainApi(compose.foundation)
    commonMainImplementation(compose.material3)
    commonMainImplementation("io.github.epicarchitect:calendar-compose-date-picker:1.0.2")
}