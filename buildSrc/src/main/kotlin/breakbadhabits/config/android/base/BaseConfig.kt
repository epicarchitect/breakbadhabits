package breakbadhabits.config.android.base

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion

fun BaseExtension.baseConfig() {
    compileSdkVersion(32)
    defaultConfig {
        minSdk = 26
        targetSdk = 32
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}