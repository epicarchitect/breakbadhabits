package convention.android

import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    compileSdkVersion(Constants.Versions.MaxSdk)

    defaultConfig {
        minSdk = Constants.Versions.MinSdk
        targetSdk = Constants.Versions.MaxSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}