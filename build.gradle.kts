plugins {
    alias(libs.plugins.jetbrains.kotlinMultiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.serialization)
    alias(libs.plugins.cashapp.sqldelight)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms)
    alias(libs.plugins.google.firebaseCrashlytics)
}

kotlin {
    jvmToolchain(21)

    androidTarget()
    listOf(
        iosX64(),
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
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.jetbrains.kotlinDatetime)
            implementation(libs.jetbrains.kotlinCoroutinesCore)
            implementation(libs.jetbrains.navigationCompose)
            implementation(libs.jetbrains.serializationJson)
            implementation(libs.cashapp.sqldelightCoroutinesExtensions)
            implementation(libs.cashapp.sqldelightPrimitiveAdapters)
            implementation(libs.epicarchitect.epicCalendarCompose)
        }
        commonTest.dependencies {
            implementation(libs.jetbrains.kotlinTest)
        }
        androidMain.dependencies {
            implementation(libs.android.coreKtx)
            implementation(libs.android.activityCompose)
            implementation(libs.cashapp.sqldelightAndroidDriver)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.crashlytics)
            implementation(libs.firebase.analytics)
        }
        iosMain.dependencies {
            implementation(libs.cashapp.sqldelightNativeDriver)
        }
    }
}

android {
    namespace = "epicarchitect.breakbadhabits"
    bundle.storeArchive.enable = true
    compileSdk = 35

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        resourceConfigurations += setOf("en", "ru")
        minSdk = 26
        targetSdk = 35
        versionCode = 84
        versionName = "4.2.0"
        base.archivesName.set("breakbadhabits-$versionName")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }

        debug {
            applicationIdSuffix = ".debug"
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