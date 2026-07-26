plugins {
    alias(libs.plugins.jetbrains.kotlinMultiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.serialization)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.cashapp.sqldelight)
}

kotlin {
    jvmToolchain(21)

    android {
        namespace = "epicarchitect.breakbadhabits.multiplatform"
        minSdk = 26
        compileSdk = 37
        withHostTest {}
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLib"
            freeCompilerArgs = listOf("-Xbinary=bundleId=$baseName")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.jetbrains.composeFoundation)
            implementation(libs.jetbrains.composeMaterial3)
            implementation(libs.jetbrains.composeMaterialIconsExtended)
            implementation(libs.jetbrains.kotlinDatetime)
            implementation(libs.jetbrains.kotlinCoroutinesCore)
            implementation(libs.jetbrains.navigationCompose)
            implementation(libs.jetbrains.serializationJson)
            implementation(libs.cashapp.sqldelightCoroutinesExtensions)
            implementation(libs.cashapp.sqldelightPrimitiveAdapters)
        }
        commonTest.dependencies {
            implementation(libs.jetbrains.kotlinTest)
        }
        iosMain.dependencies {
            implementation(libs.cashapp.sqldelightNativeDriver)
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("epicarchitect.breakbadhabits.database")
        }
    }
}