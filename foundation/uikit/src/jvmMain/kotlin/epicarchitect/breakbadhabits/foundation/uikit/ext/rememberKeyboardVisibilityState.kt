package epicarchitect.breakbadhabits.foundation.uikit.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State

@Stable
private object EmptyKeyboardVisibilityState : State<Boolean> {
    override val value = false
}

@Composable
actual fun rememberKeyboardVisibilityState(): State<Boolean> = EmptyKeyboardVisibilityState