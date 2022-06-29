plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    jvm()
}

dependencies {
    commonMainApi(project(":architecture-core"))
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
}