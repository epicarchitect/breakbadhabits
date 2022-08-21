plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

kotlin {
    android()
}

android {
    namespace = "epicarchitect.epicstore"
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}