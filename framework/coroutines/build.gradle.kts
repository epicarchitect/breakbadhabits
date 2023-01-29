plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.framework.coroutines"
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}