package epicarchitect.breakbadhabits.foundation.uikit.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.RadioButton as MaterialRadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme

@Composable
fun RadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaterialRadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(selectedColor = AppTheme.colorScheme.primary),
            onClick = onSelect
        )

        ClickableText(
            text = AnnotatedString(text),
            onClick = {
                onSelect()
            },
            style = TextStyle.Default.copy(
                color = AppTheme.colorScheme.onBackground
            )
        )
    }
}
