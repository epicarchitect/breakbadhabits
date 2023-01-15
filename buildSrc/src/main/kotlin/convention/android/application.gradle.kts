package convention.android

plugins {
    id("com.android.application")
    id("convention.android.base")
    id("kotlin-android")
}

android {
    bundle {
        storeArchive {
            enable = true
        }
    }
}