plugins {
    id("convention.android.library")
}

dependencies {
    api(projects.foundation.controllers)
    api(projects.foundation.viewmodel)
    api(projects.logic.habits)
}
