package convention.multiplatform

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.android.base")
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        create("iosMain") {
            dependsOn(getByName("commonMain"))
            getByName("iosX64Main").dependsOn(this)
            getByName("iosArm64Main").dependsOn(this)
            getByName("iosSimulatorArm64Main").dependsOn(this)
        }
    }
}