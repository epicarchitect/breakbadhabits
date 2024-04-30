plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.cashapp.sqldelight)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}


kotlin {
    sourceSets {
        getByName("androidMain") {
            dependencies {
                implementation(libs.cashapp.sqldelight.androidDriver)
            }
        }
//        getByName("iosMain") {
//            dependencies {
//                implementation(libs.cashapp.sqldelight.nativeDriver)
//            }
//        }
    }
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.sqldelight.main")
        }
    }
}

dependencies {
    commonMainApi(projects.multiplatform.foundation.datetime)
    commonMainApi(libs.cashapp.sqldelight.coroutinesExtensions)
    commonMainApi(libs.cashapp.sqldelight.primitiveAdapters)
    commonMainApi(libs.russhwolf.multiplatformSettings)
    commonMainApi(libs.russhwolf.multiplatformSettingsNoArg)
    commonMainImplementation(libs.jetbrains.kotlin.serializationJson)
}