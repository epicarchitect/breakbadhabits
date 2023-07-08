plugins {
    id("convention.multiplatform.library")
    kotlin("native.cocoapods")
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
    cocoapods {
        version = BuildConstants.APP_VERSION_NAME
        homepage = BuildConstants.PROJECT_HOMEPAGE
        summary = project.name
        ios.deploymentTarget = BuildConstants.IOS_DEPLOYMENT_TARGET
        podfile = rootProject.file("ios/Podfile")
        framework {
            baseName = "MultiplatformApp"
            isStatic = true
        }
    }
}

dependencies {
    commonMainApi(projects.multiplatform.ui.screens)
    commonMainApi(projects.multiplatform.database)
    commonMainApi(projects.multiplatform.di.holder)
    commonMainApi(projects.multiplatform.di.declarationMain)
}