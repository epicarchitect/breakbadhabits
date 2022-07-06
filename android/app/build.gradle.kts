plugins {
    id("breakbadhabits.android.application")
    id("breakbadhabits.android.compose")
    id("kotlin-kapt")
}

android {
    namespace = "breakbadhabits.android.app"

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        versionCode = 68
        versionName = "3.0.1"
    }

    signingConfigs {
        create("release") {
            storeFile = file("signing/release.jks")
            storePassword = "epicdebug"
            keyAlias = "epicdebug"
            keyPassword = "epicdebug"
        }

        getByName("debug") {
            storeFile = file("signing/debug.jks")
            storePassword = "epicdebug"
            keyAlias = "epicdebug"
            keyPassword = "epicdebug"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }

        debug {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(project(":android:compose:theme"))
    implementation(project(":android:compose:molecules"))
    implementation(project(":android:compose:organisms:histogram"))
    implementation(project(":android:compose:organisms:icons-selection"))
    implementation(project(":android:compose:organisms:events-calendar"))
    implementation(project(":android:compose:organisms:statistics"))

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.work:work-runtime-ktx:2.7.1")

    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.1.1")
    implementation("androidx.compose.material:material-icons-core:1.1.1")
    implementation("androidx.compose.material:material-icons-extended:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.4.2")

    implementation("androidx.room:room-runtime:2.4.2")
    implementation("androidx.room:room-ktx:2.4.2")
    kapt("androidx.room:room-compiler:2.4.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation("io.insert-koin:koin-core:3.2.0")
    implementation("io.insert-koin:koin-android:3.2.0")

    implementation("io.github.vanpra.compose-material-dialogs:core:0.7.0")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.7.0")
}