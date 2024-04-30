package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

@Composable
fun Dialog(
    onDismiss: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = { onDismiss?.invoke() },
    ) {
        Card(content = content)
    }
}