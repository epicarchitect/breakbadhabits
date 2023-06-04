plugins {
    id("convention.android.library")
}

dependencies {
    api(projects.foundation.math)
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
}