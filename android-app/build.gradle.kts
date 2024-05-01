plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.android)
    alias(libs.plugins.jetbrains.compose)
}

android {
    bundle {
        storeArchive {
            enable = true
        }
    }

    namespace = "epicarchitect.breakbadhabits.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        resourceConfigurations += setOf("en", "ru")
        minSdk = 26
        targetSdk = 34
        versionCode = 70
        versionName = "4.0.0"
        base.archivesName.set("breakbadhabits-$versionName")
    }

    signingConfigs {
        register("release") {
            storeFile = file("signing/release.jks")
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
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.kmpLib)
}