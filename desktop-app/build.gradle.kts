import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    alias(libs.plugins.compose)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(projects.ui.compose)
    implementation(projects.database)
    implementation(projects.di.holder)
    implementation(projects.di.declarationImpl)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinMultiplatformComposeDesktopApplication"
            packageVersion = "1.0.0"
        }
    }
}