package epicarchitect.breakbadhabits.screens.habits.eventRecords.details


import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.event_records_total_count
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.screens.habits.details.previewHabitState
import epicarchitect.breakbadhabits.uikit.PreviewTheme

@Composable
fun LazyItemScope.HabitRecordItem(
    item: HabitEventRecord
) {
    val navBackStack = LocalNavBackStack.current

    Card(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        onClick = {
            navBackStack.push(
                RootRoute.HabitEventRecordEditing(
                    habitEventRecordId = item.id,
                    habitId = item.habitId
                )
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
        Text(
            text = item.timeRange().toLocalDateTimeRange(AppTimeZone).formatted(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            modifier = Modifier.padding(top = 2.dp),
            text = stringResource(Res.string.event_records_total_count, item.eventCount),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

            if (item.comment.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = item.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 160, name = "HabitRecordItem")
@Composable
private fun HabitRecordItemPreview() {
    PreviewTheme {
        LazyColumn {
            item {
                HabitRecordItem(
                    item = previewHabitState().habitEventRecords.first()
                )
            }
        }
    }
}
