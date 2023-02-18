package breakbadhabits.framework.uikit.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import breakbadhabits.framework.uikit.ext.rememberKeyboardVisibilityState

@Composable
fun ClearFocusWhenKeyboardHiddenEffect() {
    val focusManager = LocalFocusManager.current
    val isKeyboardVisible by rememberKeyboardVisibilityState()

    LaunchedEffect(isKeyboardVisible) {
        if (!isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }
}
