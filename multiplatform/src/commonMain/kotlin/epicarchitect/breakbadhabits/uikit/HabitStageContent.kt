package epicarchitect.breakbadhabits.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_adjustment_description
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation_description
import epicarchitect.breakbadhabits.resources.habit_stage_liberation_description
import epicarchitect.breakbadhabits.resources.habit_stage_resistance_description
import epicarchitect.breakbadhabits.resources.habit_stage_rewiring_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitStage.description(): String = stringResource(
    when (this) {
        HabitStage.RESISTANCE -> Res.string.habit_stage_resistance_description
        HabitStage.ADJUSTMENT -> Res.string.habit_stage_adjustment_description
        HabitStage.REWIRING -> Res.string.habit_stage_rewiring_description
        HabitStage.CONSOLIDATION -> Res.string.habit_stage_consolidation_description
        HabitStage.LIBERATION -> Res.string.habit_stage_liberation_description
    }
)

@Preview(widthDp = 360, heightDp = 220, name = "HabitStageText")
@Composable
private fun HabitStageTextPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(HabitStage.REWIRING.title())
            Text(HabitStage.REWIRING.description())
        }
    }
}
