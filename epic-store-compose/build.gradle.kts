plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "epicarchitect.epicstore.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
}

dependencies {
    api(project(":epic-store"))
    api("androidx.compose.foundation:foundation:1.2.1")
}