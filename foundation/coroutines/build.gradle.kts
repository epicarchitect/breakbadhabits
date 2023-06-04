plugins {
    id("convention.multiplatform.library")
}

kotlin {
    sourceSets {
        getByName("desktopMain") {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")
            }
        }
    }
}

dependencies {
    commonMainApi("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
}