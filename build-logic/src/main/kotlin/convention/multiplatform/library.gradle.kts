package convention.multiplatform

import convention.Constants
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.android.base")
}

kotlin {
    android()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Constants.JVM_TARGET
    }
}