plugins {
    id("breakbadhabits.android.library")
}

android {
    namespace = "breakbadhabits.extension.coroutines"
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}