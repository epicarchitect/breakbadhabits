plugins {
    id("convention.android.library")
    id("com.google.devtools.ksp") version "1.7.20-1.0.7"
}

dependencies {
    api(project(":app-dependencies"))
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    ksp("androidx.room:room-compiler:2.4.3")
}