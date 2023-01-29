plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.entity"
}

dependencies {
    api(project(":framework:datetime"))
}