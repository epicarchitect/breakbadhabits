plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":architecture-core"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
}