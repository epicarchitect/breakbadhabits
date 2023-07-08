package convention.android

import BuildConstants
import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("convention.common.base")
}

configure<BaseExtension> {
    namespace = buildAndroidNamespace()
    compileSdkVersion(BuildConstants.TARGET_ANDROID_SDK)
    archivesName.set("${rootProject.name}-${BuildConstants.APP_VERSION_NAME}")

    defaultConfig {
        minSdk = BuildConstants.MIN_ANDROID_SDK
        targetSdk = BuildConstants.TARGET_ANDROID_SDK
        versionCode = BuildConstants.APP_VERSION_CODE
        versionName = BuildConstants.APP_VERSION_NAME
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(BuildConstants.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(BuildConstants.JVM_TARGET)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = BuildConstants.JVM_TARGET
    }
}

fun Project.buildAndroidNamespace(): String {
    val pathToModule = project.path.replace(
        regex = "[:\\-]".toRegex(),
        replacement = "."
    )

    return (BuildConstants.PROJECT_DEVELOPER_NAME + "." + rootProject.name + pathToModule).lowercase()
}