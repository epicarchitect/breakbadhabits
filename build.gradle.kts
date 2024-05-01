plugins {
    alias(libs.plugins.jetbrains.android) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.jetbrains.cocoapods) apply false
    alias(libs.plugins.jetbrains.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.cashapp.sqldelight) apply false
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(rootProject.libs.versions.jetbrains.kotlin.get())
        }
        if (requested.group == "org.jetbrains.kotlinx" && requested.name.contains("kotlinx-coroutines")) {
            useVersion(rootProject.libs.versions.jetbrains.coroutinesCore.get())
        }
    }
}