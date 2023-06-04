package convention.android

import com.android.build.gradle.BaseExtension
import convention.Constants
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

configure<BaseExtension> {
    namespace = buildAndroidNamespace()
    compileSdkVersion(Constants.TARGET_ANDROID_SDK)

    defaultConfig {
        minSdk = Constants.MIN_ANDROID_SDK
        targetSdk = Constants.TARGET_ANDROID_SDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
        targetCompatibility = JavaVersion.toVersion(Constants.JVM_TARGET)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Constants.JVM_TARGET
    }
}

fun Project.buildAndroidNamespace() = ("epicarchitect." + rootProject.name + project.path.replace(
    regex = "[:\\-]".toRegex(),
    replacement = "."
)).lowercase()