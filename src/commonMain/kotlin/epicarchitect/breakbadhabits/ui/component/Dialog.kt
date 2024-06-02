package epicarchitect.breakbadhabits.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

@Composable
fun Dialog(
    onDismiss: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(content = content)
    }
}