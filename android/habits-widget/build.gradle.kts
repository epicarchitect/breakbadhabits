plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    id("convention.android.base")
}

dependencies {
    api(projects.multiplatform.foundation.uikit)
    api(projects.multiplatform.di.holder)
}
