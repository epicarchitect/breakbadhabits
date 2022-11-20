plugins {
    id("breakbadhabits.android.library")
    id("kotlin-kapt")
}

android {
    namespace = "breakbadhabits.app.dependencies.main"
}

dependencies {
    api(project(":app-dependencies"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")
}