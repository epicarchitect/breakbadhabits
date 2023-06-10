plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    id("convention.android.base")
}

android {
    bundle {
        storeArchive {
            enable = true
        }
    }

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
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
    implementation(projects.habitsWidgetAndroid)
    implementation(projects.shared)
}