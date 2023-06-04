plugins {
    id("convention.multiplatform.library")
    kotlin("native.cocoapods")
    alias(libs.plugins.compose)
}

kotlin {
    cocoapods {
        version = "1.0.0"
        homepage = "epicarchitect"
        summary = "breakbadhabits"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios-app/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
}

dependencies {
    commonMainApi(projects.ui.compose)
    commonMainApi(projects.database)
    commonMainApi(projects.di.holder)
    commonMainApi(projects.di.declarationImpl)
}