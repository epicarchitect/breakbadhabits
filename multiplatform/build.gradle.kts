plugins {
    alias(libs.plugins.jetbrains.kotlinMultiplatform)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.serialization)
    alias(libs.plugins.android.kotlinMultiplatformLibrary)
    alias(libs.plugins.android.room)
    alias(libs.plugins.google.ksp)
}

kotlin {
    jvmToolchain(21)

    android {
        namespace = "epicarchitect.breakbadhabits.multiplatform"
        minSdk = 31
        compileSdk = 37
        androidResources.enable = true
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KMPLib"
            freeCompilerArgs = listOf("-Xbinary=bundleId=$baseName")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.epicarchitect.epicCalendarCompose)
            implementation(libs.jetbrains.composeFoundation)
            implementation(libs.jetbrains.composeResources)
            implementation(libs.jetbrains.composeMaterial3)
            implementation(libs.jetbrains.composeMaterialIconsExtended)
            implementation(libs.jetbrains.kotlinDatetime)
            implementation(libs.jetbrains.kotlinCoroutinesCore)
            implementation(libs.jetbrains.navigation3Ui)
            implementation(libs.jetbrains.serializationJson)
            implementation(libs.android.roomRuntime)
        }
        androidMain.dependencies {
            implementation(libs.android.activityCompose)
        }
        iosMain.dependencies {
            implementation(libs.android.sqliteBundled)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    compilerOptions {
        optIn.addAll(
            "kotlin.uuid.ExperimentalUuidApi"
        )
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

compose.resources {
    packageOfResClass = "epicarchitect.breakbadhabits.resources"
}

dependencies {
    add("kspAndroid", libs.android.roomCompiler)
    add("kspIosArm64", libs.android.roomCompiler)
    add("kspIosSimulatorArm64", libs.android.roomCompiler)
}