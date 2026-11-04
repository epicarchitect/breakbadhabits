package epicarchitect.breakbadhabits.screens.habits.stages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.habits.HabitProgress
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.uikit.LocalInstant
import kotlin.uuid.Uuid

@Immutable
data class HabitStagesScreenState(
    val habit: Habit,
    val progress: HabitProgress?
)

@Composable
fun rememberHabitStagesScreenState(habitId: Uuid): HabitStagesScreenState? {
    val currentTime = LocalInstant.current.value
    val habitState = remember(habitId) {
        AppDatabase.habitsDao.habitById(habitId)
    }.collectAsState(null)
    val lastRecordState = remember(habitId) {
        AppDatabase.habitEventRecordsDao.recordByHabitIdAndMaxEndTime(habitId)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val progress = lastRecordState.value
        ?.abstinence(currentTime)
        ?.let(::habitProgress)

    return remember(habit, progress) {
        HabitStagesScreenState(
            habit = habit,
            progress = progress
        )
    }
}
