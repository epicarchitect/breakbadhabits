plugins {
    id("convention.android.library")
}

dependencies {
    api(projects.foundation.coroutines)
    api(projects.foundation.datetime)
}