plugins {
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.serialization)
    alias(libs.plugins.jetbrains.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.cashapp.sqldelight)
}

android {
    namespace = "epicarchitect.breakbadhabits.kmplib"
    compileSdk = 34
    defaultConfig.minSdk = 26
    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        homepage = "null"
        summary = "null"
        ios.deploymentTarget = "14.1"
        podfile = rootProject.file("ios-app/Podfile")
        framework {
            baseName = "KMPLib"
            isStatic = true
        }
    }

    sourceSets {
        iosMain.dependencies {
            api(libs.cashapp.sqldelightNativeDriver)
        }

        androidMain.dependencies {
            api(libs.android.coreKtx)
            api(libs.android.activityCompose)
            api(libs.cashapp.sqldelightAndroidDriver)
        }

        commonMain.dependencies {
            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(libs.jetbrains.datetime)
            api(libs.jetbrains.coroutinesCore)
            api(libs.jetbrains.serializationJson)
            api(libs.cashapp.sqldelightCoroutinesExtensions)
            api(libs.cashapp.sqldelightPrimitiveAdapters)
            api(libs.russhwolf.multiplatformSettings)
            api(libs.russhwolf.multiplatformSettingsNoArg)
            api(libs.adrielcafe.voyagerNavigator)
            api(libs.epicarchitect.calendarComposeDatePicker)
        }
    }
}

sqldelight {
    databases {
        create("MainDatabase") {
            packageName.set("epicarchitect.breakbadhabits.database")
        }
    }
}