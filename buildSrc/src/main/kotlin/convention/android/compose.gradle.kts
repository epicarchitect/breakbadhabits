package convention.android

import com.android.build.gradle.BaseExtension
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
configure<BaseExtension> {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = "1.3.0-rc02"
}