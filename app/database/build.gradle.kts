plugins {
    id("convention.android.library")
    id("app.cash.sqldelight") version "2.0.0-alpha05"
}

android {
    namespace = "breakbadhabits.database"
}

dependencies {
    api("app.cash.sqldelight:coroutines-extensions:2.0.0-alpha05")
    api("app.cash.sqldelight:android-driver:2.0.0-alpha05")
    api("app.cash.sqldelight:primitive-adapters:2.0.0-alpha05")
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("breakbadhabits.app.database")
        }
    }
}