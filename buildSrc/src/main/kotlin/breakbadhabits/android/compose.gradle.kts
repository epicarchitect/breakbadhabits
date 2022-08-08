package breakbadhabits.android

import com.android.build.gradle.BaseExtension

@Suppress("UnstableApiUsage")
configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
}