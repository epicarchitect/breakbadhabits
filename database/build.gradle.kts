plugins {
    id("convention.android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("app.cash.sqldelight") version "2.0.0-rc01"
}

dependencies {
    api("app.cash.sqldelight:coroutines-extensions:2.0.0-rc01")
    api("app.cash.sqldelight:android-driver:2.0.0-rc01")
    api("app.cash.sqldelight:primitive-adapters:2.0.0-rc01")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation(projects.foundation.coroutines)
    implementation(projects.foundation.datetime)
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.sqldelight.main")
        }
    }
}