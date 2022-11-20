plugins {
    id("breakbadhabits.android.library")
}

android {
    namespace = "breakbadhabits.extension.datetime"
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}