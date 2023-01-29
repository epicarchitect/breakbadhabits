plugins {
    id("convention.android.library")
}

android {
    namespace = "breakbadhabits.presentation.habit.deletion"
}

dependencies {
    api(project(":extensions:viewmodel"))
    api(project(":logic:habit-deleter"))
}
