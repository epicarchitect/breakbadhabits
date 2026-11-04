package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_details_empty_description
import epicarchitect.breakbadhabits.resources.habit_details_empty_title
import epicarchitect.breakbadhabits.resources.habit_record_slip
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.PrimaryButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitDetailsEmptyState(
    onRecordSlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val compact = maxHeight < 420.dp
        val iconContainerSize = if (compact) 64.dp else 96.dp
        val iconSize = if (compact) 32.dp else 44.dp
        val contentSpacing = if (compact) 16.dp else 24.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = maxHeight)
                .padding(horizontal = 24.dp, vertical = contentSpacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(iconContainerSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.EventBusy,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(contentSpacing))

            Text(
                text = stringResource(Res.string.habit_details_empty_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.habit_details_empty_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(contentSpacing))

            PrimaryButton(
                text = stringResource(Res.string.habit_record_slip),
                onClick = onRecordSlip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 640, name = "HabitDetailsEmptyState")
@Composable
private fun HabitDetailsEmptyStatePreview() {
    PreviewTheme {
        HabitDetailsEmptyState(
            onRecordSlip = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(widthDp = 640, heightDp = 280, name = "HabitDetailsEmptyState compact")
@Composable
private fun HabitDetailsEmptyStateCompactPreview() {
    PreviewTheme {
        HabitDetailsEmptyState(
            onRecordSlip = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
