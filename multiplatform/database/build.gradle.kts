plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.cashapp.sqldelight)
}

kotlin {
    sourceSets {
        getByName("androidMain") {
            dependencies {
                implementation(libs.cashapp.sqldelight.androidDriver)
            }
        }
        getByName("iosMain") {
            dependencies {
                implementation(libs.cashapp.sqldelight.nativeDriver)
            }
        }
    }
}

dependencies {
    commonMainApi(libs.cashapp.sqldelight.coroutinesExtensions)
    commonMainApi(libs.cashapp.sqldelight.primitiveAdapters)
    commonMainImplementation(libs.jetbrains.kotlin.serializationJson)
    commonMainImplementation(projects.multiplatform.foundation.coroutines)
    commonMainImplementation(projects.multiplatform.foundation.datetime)
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.sqldelight.main")
        }
    }
}