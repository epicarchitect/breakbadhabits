package convention.android

import com.android.build.gradle.BaseExtension

configure<BaseExtension> {
    namespace = makeAndroidNamespace(group.toString(), name)
    compileSdkVersion(33)

    defaultConfig {
        minSdk = 26
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

fun makeAndroidNamespace(
    moduleGroupName: String,
    moduleName: String
): String {
    fun String.normalize() = filterNot { it == ' ' }.replace('-', '.').toLowerCase()
    return moduleGroupName.normalize() + "." + moduleName.normalize()
}