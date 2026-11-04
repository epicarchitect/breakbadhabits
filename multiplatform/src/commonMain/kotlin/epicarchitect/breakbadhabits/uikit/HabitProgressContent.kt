package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_stage_freedom
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_1
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_2
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_advice_3
import epicarchitect.breakbadhabits.resources.habit_stage_freedom_description
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.habitProgress
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

fun HabitProgress.titleResource(): StringResource =
    if (isComplete) Res.string.habit_stage_freedom else stage.titleResource()

@Composable
fun HabitProgress.title(): String = stringResource(titleResource())

suspend fun HabitProgress.stageNameText(): String =
    getString(titleResource())

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

@Preview(widthDp = 360, heightDp = 260, name = "HabitProgressText")
@Composable
private fun HabitProgressTextPreview() {
    val progress = habitProgress(34.days + 4.hours)
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(progress.title())
            Text(progress.description())
            progress.advice().forEach { Text("• $it") }
        }
    }
}
