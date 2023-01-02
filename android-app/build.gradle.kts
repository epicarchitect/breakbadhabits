plugins {
    id("convention.android.application")
    id("convention.android.compose")
    id("kotlin-kapt")
}

android {
    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        versionCode = 70
        versionName = "4.0.0"
    }

    signingConfigs {
        create("release") {
            storeFile = file("signing/release.jks")
            storePassword = "epicdebug"
            keyAlias = "epicdebug"
            keyPassword = "epicdebug"
        }

        getByName("debug") {
            storeFile = file("signing/debug.jks")
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
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(project(":ui-kit"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    implementation(project(":presentation:current-habit-abstinence"))
    implementation(project(":presentation:habit-creation"))
    implementation(project(":presentation:habit-deletion"))
    implementation(project(":presentation:habit-ids"))
    implementation(project(":presentation:habit"))
    implementation(project(":presentation:habit-track"))
    implementation(project(":presentation:habit-track-creation"))
}