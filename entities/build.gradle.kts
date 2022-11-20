plugins {
    id("breakbadhabits.android.library")
}

android {
    namespace = "breakbadhabits.entity"
}

dependencies {
    api(project(":extensions:datetime"))
}