plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.compose)
    id("convention.android.base")
}

dependencies {
    api(projects.multiplatform.foundation.uikit)
    api(projects.multiplatform.di.holder)
    api(projects.multiplatform.ui.screens)
}