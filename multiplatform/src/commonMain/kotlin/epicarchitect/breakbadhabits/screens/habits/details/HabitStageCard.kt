package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_advice_title
import epicarchitect.breakbadhabits.resources.habit_stages_title
import epicarchitect.breakbadhabits.uikit.advice
import epicarchitect.breakbadhabits.uikit.description
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitStageCard(
    progress: HabitProgress,
    onAllStagesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.habit_stage_advice_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = progress.description(),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            progress.advice().forEach { advice ->
                Text(
                    text = "• $advice",
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onAllStagesClick) {
                    Text(text = stringResource(Res.string.habit_stages_title))
                }
            }
        }
    }
}
