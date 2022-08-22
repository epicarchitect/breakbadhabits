plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    namespace = "epicarchitect.epicstore.navigation.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
}

dependencies {
    api(project(":epic-store-compose"))
    api("androidx.navigation:navigation-compose:2.5.1")
}