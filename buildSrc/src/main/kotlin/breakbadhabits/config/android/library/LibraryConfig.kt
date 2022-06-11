package breakbadhabits.config.android.library

import breakbadhabits.config.android.base.baseConfig
import com.android.build.gradle.LibraryExtension

fun LibraryExtension.libraryConfig() {
    baseConfig()
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
}