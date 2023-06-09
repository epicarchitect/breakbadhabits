package convention.android

import com.android.build.gradle.BaseExtension
import convention.Constants

configure<BaseExtension> {
    namespace = buildAndroidNamespace(prefix = "epicarchitect")
    compileSdkVersion(Constants.TARGET_ANDROID_SDK)

    defaultConfig {
        minSdk = Constants.MIN_ANDROID_SDK
        targetSdk = Constants.TARGET_ANDROID_SDK
    }
}

fun Project.buildAndroidNamespace(prefix: String): String {
    val pathToModule = project.path.replace(
        regex = "[:\\-]".toRegex(),
        replacement = "."
    )

    return (prefix + "." + rootProject.name + pathToModule).lowercase()
}