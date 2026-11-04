import java.util.Properties

val secrets = Properties().apply {
    val file = rootProject.file("secrets.properties")
    file.reader().use { load(it) }
}

fun secret(property: String): String =
    secrets.getProperty(property)?.takeIf { it.isNotBlank() } ?: ""

val signingStoreFilePath = secret("signing.storeFile")
val signingKeyAlias = secret("signing.keyAlias")
val signingStorePassword = secret("signing.storePassword")
val signingKeyPassword = secret("signing.keyPassword")
val signingStoreFile = rootProject.file(signingStoreFilePath)
val rustoreConsoleAppId = secret("rustore.consoleAppId")
val appMetricaApiKey = secret("appmetrica.apiKey")
val appMetricaPostApiKey = secret("appmetrica.postApiKey")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.composeCompiler)
    alias(libs.plugins.appmetrica)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "epicarchitect.breakbadhabits"
    bundle.storeArchive.enable = true
    compileSdk = 37

    defaultConfig {
        applicationId = "kolmachikhin.alexander.breakbadhabits"
        minSdk = 31
        targetSdk = 37
        versionCode = 87
        versionName = "5.0.0"
        base.archivesName.set("breakbadhabits-$versionName")
        buildConfigField("String", "APPMETRICA_API_KEY", "\"$appMetricaApiKey\"")
    }

    flavorDimensions += "store"

    productFlavors {
        create("googleplay") {
            dimension = "store"
            buildConfigField("String", "TARGET_STORE", "\"googleplay\"")
        }

        create("rustore") {
            dimension = "store"
            buildConfigField("String", "TARGET_STORE", "\"rustore\"")
            manifestPlaceholders["consoleAppId"] = rustoreConsoleAppId
            manifestPlaceholders["payScheme"] = "kolmachikhin.alexander.breakbadhabits.pay"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = signingStoreFile
            keyAlias = signingKeyAlias
            storePassword = signingStorePassword
            keyPassword = signingKeyPassword
        }
        create("release") {
            storeFile = signingStoreFile
            keyAlias = signingKeyAlias
            storePassword = signingStorePassword
            keyPassword = signingKeyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            appmetrica {
                enable.set(false)
                enableAnalytics.set(false)
                postApiKey.set(appMetricaPostApiKey)
            }
        }

        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
            appmetrica {
                enable.set(false)
                enableAnalytics.set(false)
            }
        }
    }
}

dependencies {
    implementation(projects.multiplatform)
    implementation(libs.android.activityCompose)
    implementation(libs.android.roomRuntime)
    implementation(libs.android.sqliteBundled)
    implementation(libs.jetbrains.kotlinDatetime)
    implementation(libs.appmetrica.analytics)
    "rustoreImplementation"(libs.rustore.pay)
}

configurations.configureEach {
    exclude(group = "io.appmetrica.analytics", module = "analytics-ad-revenue")
    exclude(group = "io.appmetrica.analytics", module = "analytics-apphud")
    exclude(group = "io.appmetrica.analytics", module = "analytics-billing")
    exclude(group = "io.appmetrica.analytics", module = "analytics-id-sync")
    exclude(group = "io.appmetrica.analytics", module = "analytics-location")
    exclude(group = "io.appmetrica.analytics", module = "analytics-ndkcrashes")
    exclude(group = "io.appmetrica.analytics", module = "analytics-screenshot")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk7")
}