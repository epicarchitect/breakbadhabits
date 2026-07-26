package epicarchitect.breakbadhabits.screens.habits.eventRecords.details


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.screens.root.LocalRootNavController
import epicarchitect.breakbadhabits.screens.root.RootRoute

@Composable
fun LazyItemScope.HabitRecordItem(
    item: HabitEventRecord
) {
    val environment = LocalAppEnvironment.current
    val navController = LocalRootNavController.current
    val dateTimeFormatter = environment.format.dateTimeFormatter
    val strings = environment.resources.strings.habitEventRecordsDashboardStrings
    val timeZone = environment.dateTime.currentTimeZone()

    Box(
        modifier = Modifier
            .animateItem()
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    RootRoute.HabitEventRecordEditing(
                        habitEventRecordId = item.id,
                        habitId = item.habitId
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier.padding(
                start = 14.dp,
                end = 14.dp,
                top = 4.dp,
                bottom = 4.dp
            )
        ) {
            Text(
                modifier = Modifier.padding(2.dp),
                text = dateTimeFormatter.format(item.timeRange().toLocalDateTimeRange(timeZone)),
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                modifier = Modifier.padding(2.dp),
                text = strings.eventCount(item.eventCount),
                style = MaterialTheme.typography.bodySmall
            )

            if (item.comment.isNotBlank()) {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = item.comment,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}