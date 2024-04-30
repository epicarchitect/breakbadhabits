plugins {
    id("convention.multiplatform.library")
//    kotlin("native.cocoapods")
    alias(libs.plugins.jetbrains.compose)
}

kotlin {
//    cocoapods {
//        version = BuildConstants.APP_VERSION_NAME
//        homepage = BuildConstants.PROJECT_HOMEPAGE
//        summary = project.name
//        ios.deploymentTarget = BuildConstants.IOS_DEPLOYMENT_TARGET
//        podfile = rootProject.file("ios/Podfile")
//        framework {
//            baseName = "MultiplatformApp"
//            isStatic = true
//        }
//    }
}

dependencies {
    commonMainApi(projects.multiplatform.database)
    commonMainApi(libs.adrielcafe.voyager.navigator)
    commonMainApi(projects.multiplatform.foundation.uikit)
    commonMainImplementation(compose.materialIconsExtended)
//    commonMainApi(projects.multiplatform.ui.icons)
//    commonMainApi(projects.multiplatform.di.holder)
//    commonMainApi(projects.multiplatform.presentation.dashboard)
//    commonMainApi(projects.multiplatform.presentation.habits)
//    commonMainApi(projects.multiplatform.presentation.app)
//    commonMainApi(projects.multiplatform.di.declaration)
//    commonMainApi(projects.multiplatform.di.holder)
//    commonMainApi(projects.multiplatform.di.declarationMain)
//    commonMainApi(projects.multiplatform.presentation.app)
}