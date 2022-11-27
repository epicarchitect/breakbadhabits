plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":logic:core"))
    api(project(":logic:habit-new-name-validator"))
    api(project(":logic:habit-track-interval-validator"))
}