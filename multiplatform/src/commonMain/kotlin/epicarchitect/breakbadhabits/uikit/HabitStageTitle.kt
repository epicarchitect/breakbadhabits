package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_adjustment
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation
import epicarchitect.breakbadhabits.resources.habit_stage_liberation
import epicarchitect.breakbadhabits.resources.habit_stage_resistance
import epicarchitect.breakbadhabits.resources.habit_stage_rewiring
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

fun HabitStage.titleResource(): StringResource = when (this) {
    HabitStage.RESISTANCE -> Res.string.habit_stage_resistance
    HabitStage.ADJUSTMENT -> Res.string.habit_stage_adjustment
    HabitStage.REWIRING -> Res.string.habit_stage_rewiring
    HabitStage.CONSOLIDATION -> Res.string.habit_stage_consolidation
    HabitStage.LIBERATION -> Res.string.habit_stage_liberation
}

@Composable
fun HabitStage.title(): String = stringResource(titleResource())
