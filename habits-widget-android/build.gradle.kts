plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    id("convention.android.base")
}

dependencies {
    api(projects.foundation.uikit)
    api(projects.di.holder)
}