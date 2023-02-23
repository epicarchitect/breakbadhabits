package breakbadhabits.foundation.uikit.activity

import androidx.compose.runtime.compositionLocalOf

val LocalDarkModeManager = compositionLocalOf<DarkModeManager> {
    error("DarkModeManager not provided")
}

val LocalComposeActivity = compositionLocalOf<ComposeActivity> {
    error("ComposeActivity not provided")
}