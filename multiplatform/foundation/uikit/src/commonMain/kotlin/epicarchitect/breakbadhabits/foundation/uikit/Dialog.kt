package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay

@Composable
fun Dialog(
    onDismiss: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var shown by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        delay(50)
        shown = true
    }

    Popup(
        content = {
            Card {
                content()
            }
        },
        onDismissRequest = onDismiss,
        alignment = Alignment.Center
    )
}