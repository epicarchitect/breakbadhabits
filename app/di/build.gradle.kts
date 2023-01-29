plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.di"
}

dependencies {
    api(project(":app:presentation:habit-creation"))
    api(project(":app:presentation:habit-deletion"))
    api(project(":app:presentation:dashboard"))
    api(project(":app:presentation:habit-details"))
    api(project(":app:presentation:habit-track-details"))
    api(project(":app:presentation:habit-track-creation"))
    implementation(project(":app:database"))
}