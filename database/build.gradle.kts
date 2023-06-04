plugins {
    id("convention.multiplatform.library")
    id("convention.android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("app.cash.sqldelight") version "2.0.0-rc01"
}

kotlin {
    android()

    sourceSets {
        getByName("androidMain") {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.0-rc01")
            }
        }
    }
}

dependencies {
    commonMainApi("app.cash.sqldelight:coroutines-extensions:2.0.0-rc01")
    commonMainApi("app.cash.sqldelight:primitive-adapters:2.0.0-rc01")
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    commonMainImplementation(projects.foundation.coroutines)
    commonMainImplementation(projects.foundation.datetime)
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.sqldelight.main")
        }
    }
}