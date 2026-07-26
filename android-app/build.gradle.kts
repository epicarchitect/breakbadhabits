plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.google.gms)
    alias(libs.plugins.google.firebaseCrashlytics)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "epicarchitect.breakbadhabits"
    bundle.storeArchive.enable = true
    compileSdk = 37

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        minSdk = 26
        targetSdk = 37
        versionCode = 85
        versionName = "4.3.0"
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

dependencies {
    implementation(projects.multiplatform)
    implementation(libs.android.activityCompose)
    implementation(libs.cashapp.sqldelightAndroidDriver)
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.jetbrains.kotlinDatetime)
}