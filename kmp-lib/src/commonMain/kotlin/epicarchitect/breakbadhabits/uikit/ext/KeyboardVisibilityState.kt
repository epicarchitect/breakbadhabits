package epicarchitect.breakbadhabits.uikit.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun rememberKeyboardVisibilityState(): State<Boolean>