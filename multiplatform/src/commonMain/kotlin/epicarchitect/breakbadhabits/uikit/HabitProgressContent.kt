package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_freedom
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun HabitProgress.title(): String =
    if (isComplete) stringResource(Res.string.habit_stage_freedom) else stage.title()

@Composable
fun HabitProgress.description(): String =
    if (isComplete) {
        stringResource(Res.string.habit_stage_freedom_description)
    } else {
        stage.description()
    }

@Composable
fun HabitProgress.advice(): List<String> =
    if (isComplete) {
        listOf(
            stringResource(Res.string.habit_stage_freedom_advice_1),
            stringResource(Res.string.habit_stage_freedom_advice_2),
            stringResource(Res.string.habit_stage_freedom_advice_3)
        )
    } else {
        stage.advice()
    }
