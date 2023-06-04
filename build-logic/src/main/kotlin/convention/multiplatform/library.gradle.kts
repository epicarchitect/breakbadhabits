package convention.multiplatform

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("convention.android.base")
}

kotlin {
    android()
    jvm("desktop")
}