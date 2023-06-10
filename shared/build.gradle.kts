plugins {
    id("convention.multiplatform.library")
    kotlin("native.cocoapods")
    alias(libs.plugins.compose)
}

kotlin {
    cocoapods {
        version = BuildConstants.APP_VERSION_NAME
        homepage =  BuildConstants.PROJECT_HOMEPAGE
        summary = project.name
        ios.deploymentTarget = BuildConstants.IOS_DEPLOYMENT_TARGET
        podfile = project.file("../ios-app/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
}

dependencies {
    commonMainApi(projects.ui.screens)
    commonMainApi(projects.database)
    commonMainApi(projects.di.holder)
    commonMainApi(projects.di.declarationMain)
}