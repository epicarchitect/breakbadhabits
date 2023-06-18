plugins {
    `kotlin-dsl`
}

dependencies {
    // workaround for libs https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.arturbosch.detektGradlePlugin)
}