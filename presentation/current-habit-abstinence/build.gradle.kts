plugins {
    id("convention.android.library")
}

dependencies {
    api(project(":presentation:core"))
    api(project(":presentation:habit-abstinence-formatter"))
    api(project(":logic:current-habit-abstinence-provider"))
}
