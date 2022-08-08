plugins {
    id("breakbadhabits.multiplatform.library")
}

android {
    namespace = "breakbadhabits.coroutines"
}

dependencies {
    commonMainApi("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")
}