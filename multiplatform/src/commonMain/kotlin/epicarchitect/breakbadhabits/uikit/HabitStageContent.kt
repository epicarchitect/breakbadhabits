package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitStage
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_acute_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_acute_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_acute_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_acute_description
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_consolidation_description
import epicarchitect.breakbadhabits.resources.habit_stage_final_consolidation_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_final_consolidation_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_final_consolidation_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_final_consolidation_description
import epicarchitect.breakbadhabits.resources.habit_stage_rebuilding_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_rebuilding_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_rebuilding_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_rebuilding_description
import epicarchitect.breakbadhabits.resources.habit_stage_withdrawal_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_withdrawal_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_withdrawal_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_withdrawal_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitStage.description(): String = stringResource(
    when (this) {
        HabitStage.ACUTE -> Res.string.habit_stage_acute_description
        HabitStage.WITHDRAWAL -> Res.string.habit_stage_withdrawal_description
        HabitStage.REBUILDING -> Res.string.habit_stage_rebuilding_description
        HabitStage.CONSOLIDATION -> Res.string.habit_stage_consolidation_description
        HabitStage.FINAL_CONSOLIDATION -> Res.string.habit_stage_final_consolidation_description
    }
)

@Composable
fun HabitStage.advice(): List<String> = when (this) {
    HabitStage.ACUTE -> listOf(
        stringResource(Res.string.habit_stage_acute_advice_1),
        stringResource(Res.string.habit_stage_acute_advice_2),
        stringResource(Res.string.habit_stage_acute_advice_3)
    )

    HabitStage.WITHDRAWAL -> listOf(
        stringResource(Res.string.habit_stage_withdrawal_advice_1),
        stringResource(Res.string.habit_stage_withdrawal_advice_2),
        stringResource(Res.string.habit_stage_withdrawal_advice_3)
    )

    HabitStage.REBUILDING -> listOf(
        stringResource(Res.string.habit_stage_rebuilding_advice_1),
        stringResource(Res.string.habit_stage_rebuilding_advice_2),
        stringResource(Res.string.habit_stage_rebuilding_advice_3)
    )

    HabitStage.CONSOLIDATION -> listOf(
        stringResource(Res.string.habit_stage_consolidation_advice_1),
        stringResource(Res.string.habit_stage_consolidation_advice_2),
        stringResource(Res.string.habit_stage_consolidation_advice_3)
    )

    HabitStage.FINAL_CONSOLIDATION -> listOf(
        stringResource(Res.string.habit_stage_final_consolidation_advice_1),
        stringResource(Res.string.habit_stage_final_consolidation_advice_2),
        stringResource(Res.string.habit_stage_final_consolidation_advice_3)
    )
}
