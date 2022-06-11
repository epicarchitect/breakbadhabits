package breakbadhabits.config.android.base

import com.android.build.gradle.BaseExtension

fun BaseExtension.baseComposeConfig() {
    buildFeatures.compose = true

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}