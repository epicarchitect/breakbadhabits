plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "epicarchitect.epicstore.compose"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
}

dependencies {
    api(project(":epic-store"))
    api("androidx.compose.runtime:runtime:1.2.1")
}