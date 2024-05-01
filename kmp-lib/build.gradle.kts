plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.serialization)
    alias(libs.plugins.cashapp.sqldelight)
}

android {
    namespace = "epicarchitect.breakbadhabits.kmplib"
    compileSdk = 34
    defaultConfig.minSdk = 26
}

kotlin {
    jvmToolchain(17)
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLib"
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(compose.foundation)
            api(compose.material3)
            api(compose.materialIconsExtended)
            api(libs.jetbrains.datetime)
            api(libs.jetbrains.coroutinesCore)
            api(libs.jetbrains.serializationJson)
            api(libs.cashapp.sqldelightRuntime)
            api(libs.cashapp.sqldelightCoroutinesExtensions)
            implementation(libs.cashapp.sqldelightPrimitiveAdapters)
            implementation(libs.russhwolf.multiplatformSettings)
            implementation(libs.russhwolf.multiplatformSettingsNoArg)
            implementation(libs.adrielcafe.voyagerNavigator)
            implementation(libs.epicarchitect.calendarComposeDatePicker)
        }
        androidMain.dependencies {
            api(libs.android.coreKtx)
            api(libs.android.activityCompose)
            implementation(libs.cashapp.sqldelightAndroidDriver)
        }
        iosMain.dependencies {
            implementation(libs.cashapp.sqldelightNativeDriver)
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