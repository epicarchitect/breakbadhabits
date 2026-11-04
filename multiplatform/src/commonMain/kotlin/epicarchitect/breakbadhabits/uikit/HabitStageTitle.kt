package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_acute
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation
import epicarchitect.breakbadhabits.resources.habit_stage_final_consolidation
import epicarchitect.breakbadhabits.resources.habit_stage_rebuilding
import epicarchitect.breakbadhabits.resources.habit_stage_withdrawal
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitStage.title(): String = stringResource(
    when (this) {
        HabitStage.ACUTE -> Res.string.habit_stage_acute
        HabitStage.WITHDRAWAL -> Res.string.habit_stage_withdrawal
        HabitStage.REBUILDING -> Res.string.habit_stage_rebuilding
        HabitStage.CONSOLIDATION -> Res.string.habit_stage_consolidation
        HabitStage.FINAL_CONSOLIDATION -> Res.string.habit_stage_final_consolidation
    }
)
