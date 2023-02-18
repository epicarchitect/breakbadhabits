plugins {
    id("convention.android.library")
    id("com.squareup.sqldelight") version "1.5.4"
}

android {
    namespace = "breakbadhabits.database"
}

dependencies {
    api("com.squareup.sqldelight:coroutines-extensions:1.5.4")
    api("com.squareup.sqldelight:android-driver:1.5.4")
}

sqldelight {
    database("AppDatabase") {
        packageName = "breakbadhabits.app.database"
    }
}