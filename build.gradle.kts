plugins {
    alias(libs.plugins.jetbrains.multiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.cashapp.sqldelight)
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
            freeCompilerArgs = listOf("-Xbinary=bundleId=$baseName")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.jetbrains.datetime)
            implementation(libs.jetbrains.coroutinesCore)
            implementation(libs.cashapp.sqldelightCoroutinesExtensions)
            implementation(libs.cashapp.sqldelightPrimitiveAdapters)
            implementation(libs.adrielcafe.voyagerNavigator)
            implementation(libs.adrielcafe.voyagerTransitions)
            implementation(libs.epicarchitect.calendarComposeDatePicker)
        }
        androidMain.dependencies {
            implementation(libs.android.coreKtx)
            implementation(libs.android.appcompat)
            implementation(libs.android.activityCompose)
            implementation(libs.cashapp.sqldelightAndroidDriver)
        }
        iosMain.dependencies {
            implementation(libs.cashapp.sqldelightNativeDriver)
        }
    }
}

android {
    namespace = "epicarchitect.breakbadhabits"
    bundle.storeArchive.enable = true
    compileSdk = 34

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        resourceConfigurations += setOf("en", "ru")
        minSdk = 30
        targetSdk = 34
        versionCode = 70
        versionName = "4.0.0"
        base.archivesName.set("breakbadhabits-$versionName")
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }

    signingConfigs {
        register("release") {
            storeFile = rootProject.file("src/androidMain/signing/release.jks")
            storePassword = "epicdebug"
            keyAlias = "epicdebug"
            keyPassword = "epicdebug"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "src/androidMain/proguard-rules.pro"
            )
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("epicarchitect.breakbadhabits.environment.database")
        }
    }
}