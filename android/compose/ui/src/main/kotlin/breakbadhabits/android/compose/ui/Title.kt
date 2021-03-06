package breakbadhabits.android.compose.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import breakbadhabits.android.compose.ui.Text

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.h6
    )
}