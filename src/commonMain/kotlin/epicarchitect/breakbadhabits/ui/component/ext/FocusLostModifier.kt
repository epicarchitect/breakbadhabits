package epicarchitect.breakbadhabits.ui.component.ext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged

fun Modifier.onFocusLost(onFocusLost: () -> Unit) = composed {
    var lastFocusState by remember { mutableStateOf<FocusState?>(null) }
    onFocusChanged { currentFocusState ->
        if (lastFocusState?.hasFocus == true && !currentFocusState.hasFocus) {
            onFocusLost()
        }
        lastFocusState = currentFocusState
    }
}