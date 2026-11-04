package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_freedom
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

fun HabitProgress.titleResource(): StringResource =
    if (isComplete) Res.string.habit_freedom else stage.titleResource()

@Composable
fun HabitProgress.title(): String = stringResource(titleResource())

suspend fun HabitProgress.stageNameText(): String = getString(titleResource())
