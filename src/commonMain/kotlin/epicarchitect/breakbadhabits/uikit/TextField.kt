package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun InputCard(
    title: String,
    description: String,
    error: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val horizontalPadding = PaddingValues(horizontal = 16.dp)
    Card(modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontalPadding),
            text = title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            modifier = Modifier.padding(horizontalPadding),
            text = description,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            content = content
        )

        AnimatedVisibility(
            visible = error != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val color by animateColorAsState(
                targetValue = if (error != null) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Unspecified
                },
            )
            Text(
                modifier = Modifier
                    .padding(horizontalPadding)
                    .padding(top = 8.dp),
                text = error ?: "",
                color = color,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}