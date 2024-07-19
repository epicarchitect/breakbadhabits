package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    onDismiss: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Card(content = content)
    }
}