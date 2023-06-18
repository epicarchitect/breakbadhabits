package epicarchitect.breakbadhabits.foundation.uikit

import androidx.compose.material3.Checkbox as MaterialCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    MaterialCheckbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled
    )
}
