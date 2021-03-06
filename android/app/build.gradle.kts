plugins {
    id("breakbadhabits.android.application")
    id("breakbadhabits.android.compose")
    id("kotlin-kapt")
}

android {
    namespace = "breakbadhabits.android.app"

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        versionCode = 68
        versionName = "3.0.1"
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
    implementation(project(":android:compose:ui"))
    implementation(project(":android:compose:activity"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.work:work-runtime-ktx:2.7.1")
    implementation("androidx.navigation:navigation-compose:2.5.0")

    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.insert-koin:koin-android:3.2.0")

    implementation("io.github.vanpra.compose-material-dialogs:core:0.7.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.7.0")
}