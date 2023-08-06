plugins {
    id("convention.multiplatform.library")
    alias(libs.plugins.jetbrains.compose)
}

dependencies {
    commonMainImplementation(compose.materialIconsExtended)
    commonMainApi(projects.multiplatform.foundation.uikit)
}