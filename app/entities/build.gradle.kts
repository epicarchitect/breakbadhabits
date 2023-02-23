plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.entity"
}

dependencies {
    api(projects.foundation.datetime)
}