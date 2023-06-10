plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.appcash.sqldelight)
}

kotlin {
    sourceSets {
        getByName("androidMain") {
            dependencies {
                implementation(libs.appcash.sqldelight.androidDriver)
            }
        }
        getByName("iosMain") {
            dependencies {
                implementation(libs.appcash.sqldelight.nativeDriver)
            }
        }
    }
}

dependencies {
    commonMainApi(libs.appcash.sqldelight.coroutinesExtensions)
    commonMainApi(libs.appcash.sqldelight.primitiveAdapters)
    commonMainImplementation(libs.kotlin.serialization.json)
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