// todo: looks like shit
plugins {
    alias(libs.plugins.jetbrains.kotlinMultiplatform) apply false
    alias(libs.plugins.jetbrains.composeCompiler) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.serialization) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlinMultiplatformLibrary) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.appmetrica) apply false
}