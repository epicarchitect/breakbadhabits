package epicarchitect.breakbadhabits.foundation.uikit.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

private object Empty : State<Boolean> {
    override val value = false
}

@Composable
actual fun rememberKeyboardVisibilityState(): State<Boolean> = Empty