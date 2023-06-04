plugins {
    id("convention.android.application")
    id("convention.multiplatform.compose")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    bundle {
        storeArchive {
            enable = true
        }
    }

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        versionCode = 70
        versionName = "4.0.0"
        base.archivesName.set("breakbadhabits-$versionName")
        resourceConfigurations.addAll(listOf("en", "ru"))
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
}

dependencies {
    implementation(projects.foundation.uikit)
    implementation(projects.presentation.dashboard)
    implementation(projects.presentation.habits)
    implementation(projects.database)
}