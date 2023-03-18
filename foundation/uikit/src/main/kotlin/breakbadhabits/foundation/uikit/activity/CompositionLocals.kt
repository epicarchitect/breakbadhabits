package breakbadhabits.foundation.uikit.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.compositionLocalOf

val LocalDarkModeManager = compositionLocalOf<DarkModeManager> {
    error("DarkModeManager not provided")
}

val LocalActivity = compositionLocalOf<AppCompatActivity> {
    error("ComposeActivity not provided")
}