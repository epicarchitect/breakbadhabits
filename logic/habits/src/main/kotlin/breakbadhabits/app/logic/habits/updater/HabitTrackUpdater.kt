package breakbadhabits.app.logic.habits.updater

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import breakbadhabits.foundation.datetime.atMiddleOfDay
import breakbadhabits.foundation.datetime.atStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val appDatabase: AppDatabase,
    private val dateTimeConfigProvider: DateTimeConfigProvider
) {
    suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        time: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        val appTimeZone = dateTimeConfigProvider.getConfig().appTimeZone
        appDatabase.habitTrackQueries.update(
            id = id.value,
            startTimeInSecondsUtc = time.data.start.atMiddleOfDay(appTimeZone).epochSeconds,
            endTimeInSecondsUtc = time.data.endInclusive.atMiddleOfDay(appTimeZone).epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}