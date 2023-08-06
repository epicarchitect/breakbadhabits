plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.jetbrains.kotlin.serialization)
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
    commonMainApi(projects.multiplatform.foundation.coroutines)
    commonMainApi(projects.multiplatform.foundation.datetime)
    commonMainApi(libs.cashapp.sqldelight.coroutinesExtensions)
    commonMainApi(libs.cashapp.sqldelight.primitiveAdapters)
    commonMainImplementation(libs.jetbrains.kotlin.serializationJson)
}