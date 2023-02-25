package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.foundation.datetime.millisDistanceBetween
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class HabitCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        iconResource: Habit.IconResource,
        countability: HabitCountability,
        firstTrackInterval: CorrectHabitTrackRange
    ) = withContext(Dispatchers.IO) {
        appDatabase.transaction {
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()

            appDatabase.habitQueries.insert(
                id = habitId,
                name = name.data.value,
                iconId = iconResource.iconId,
                isCountable = countability is HabitCountability.Countable
            )

            appDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                rangeStart = firstTrackInterval.data.value.start.toMillis(),
                rangeEnd = firstTrackInterval.data.value.endInclusive.toMillis(),
                minutelyValue = when (countability) {
                    is HabitCountability.Countable -> dailyToMinutelyValue(
                        firstTrackInterval.data.value,
                        countability.averageDailyValue
                    )
                    is HabitCountability.Uncountable -> 1.0
                },
                comment = null
            )
        }
    }

    private fun dailyToMinutelyValue(
        range: ClosedRange<LocalDateTime>,
        dailyValue: Double
    ): Double {
        val minuteRangeDistance = millisDistanceBetween(range) / 1000 / 60
        return dailyValue / minuteRangeDistance
    }
}