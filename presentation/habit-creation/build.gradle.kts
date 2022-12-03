plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":presentation:core"))
    api(project(":logic:habit-creator"))
    api(project(":logic:habit-icons-provider"))
}
