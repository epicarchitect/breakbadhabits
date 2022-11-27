plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":presentation:core"))
    api(project(":presentation:habit-track-interval-formatter"))
    api(project(":logic:habit-creator"))
    api(project(":logic:habit-icons-provider"))
}
