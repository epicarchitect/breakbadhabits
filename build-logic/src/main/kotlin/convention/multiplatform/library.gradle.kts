package convention.multiplatform

import convention.Constants
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Constants.JVM_TARGET
    }
}