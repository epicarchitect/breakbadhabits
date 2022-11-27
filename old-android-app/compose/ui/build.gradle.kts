plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

android {
    namespace = "breakbadhabits.android.compose.ui"
}

dependencies {
    api("androidx.compose.foundation:foundation:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")
    implementation("androidx.compose.material:material-icons-extended:1.2.0")
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.6.0")
}