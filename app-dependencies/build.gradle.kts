plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.app.dependecies"
}

dependencies {
    implementation(project(":database"))
    api(project(":presentation:habit-creation"))
    api(project(":presentation:habit-deletion"))
    api(project(":presentation:habits-dashboard"))
    api(project(":presentation:habit-details"))
    api(project(":presentation:habit-track"))
    api(project(":presentation:habit-track-creation"))
}