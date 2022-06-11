package breakbadhabits.android.compose.molecule

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.RadioButton as MaterialRadioButton

@Composable
fun RadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default.copy(
        color = MaterialTheme.colors.onBackground
    )
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MaterialRadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
            onClick = {
                onSelect()
            }
        )

        ClickableText(
            text = AnnotatedString(text),
            onClick = {
                onSelect()
            },
            style = textStyle,
        )
    }
}