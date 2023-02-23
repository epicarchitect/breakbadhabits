plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.foundation.datetime"
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}