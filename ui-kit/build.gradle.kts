plugins {
    id("convention.android.library")
    id("convention.android.compose")
}

dependencies {
    api(project(":extensions:datetime"))
    api(project(":epic-store:google-navigation-compose"))
    api("androidx.activity:activity-compose:1.6.1")
    api("androidx.appcompat:appcompat:1.5.1")
    api("androidx.compose.foundation:foundation:1.3.1")
    api("androidx.core:core-ktx:1.9.0")
    api("io.github.vanpra.compose-material-dialogs:core:0.8.1-rc")
    api("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")

    implementation("androidx.compose.material:material:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation("io.github.boguszpawlowski.composecalendar:composecalendar:0.6.0")
}