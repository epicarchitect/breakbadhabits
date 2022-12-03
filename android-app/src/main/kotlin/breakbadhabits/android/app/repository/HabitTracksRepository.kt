package breakbadhabits.android.app.repository

import breakbadhabits.android.app.database.HabitTracksDao
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.mapItems
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import breakbadhabits.android.app.database.HabitTrack as DatabaseHabitTrack

class HabitTracksRepository(
    private val idGenerator: IdGenerator,
    private val habitTracksDao: HabitTracksDao
) {
    suspend fun insertHabitTrack(
        habitId: Habit.Id,
        interval: HabitTrack.Interval,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) = withContext(Dispatchers.IO) {
        habitTracksDao.insert(
            HabitTrack(
                HabitTrack.Id(idGenerator.nextId()),
                habitId,
                interval,
                dailyCount,
                comment
            ).toDatabaseHabitTrack()
        )
    }

    suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        interval: HabitTrack.Interval,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) = withContext(Dispatchers.IO) {
        habitTracksDao.update(
            id = id.value,
            startDay = interval.value.start.toString(),
            endDay = interval.value.start.toString(),
            dailyCount = dailyCount.value,
            comment = comment?.value
        )
    }

    suspend fun deleteHabitTracksByHabitId(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.deleteEntitiesByHabitId(id.value)
    }

    suspend fun deleteHabitTrackById(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.deleteEntityById(id.value)
    }

    suspend fun getHabitTrackById(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.getEntityById(id.value)?.toHabitTrack()
    }

    fun habitTrackFlowById(id: HabitTrack.Id) =
        habitTracksDao.entityByIdFlow(id.value).map { it?.toHabitTrack() }

    fun habitTracksFlowByHabitId(id: Habit.Id) =
        habitTracksDao.entityListByHabitIdFlow(id.value).mapItems { it.toHabitTrack() }

    fun habitTrackFlowByHabitIdAndLastByTime(id: Habit.Id) =
        habitTracksDao.entityListByHabitIdFlow(id.value).map { tracks ->
            tracks.maxByOrNull {
                it.endDay.toLocalDateTime()
            }?.toHabitTrack()
        }

    fun habitTracksFlow() = habitTracksDao.entityListFlow().mapItems { it.toHabitTrack() }

    private fun HabitTrack.toDatabaseHabitTrack() = DatabaseHabitTrack(
        id = id.value,
        habitId = habitId.value,
        startDay = interval.value.start.toString(),
        endDay = interval.value.end.toString(),
        dailyCount = dailyCount.value,
        comment = comment?.value
    )

    private fun DatabaseHabitTrack.toHabitTrack() = HabitTrack(
        id = HabitTrack.Id(id),
        habitId = Habit.Id(habitId),
        interval = HabitTrack.Interval(
            LocalDateTimeInterval(
                start = startDay.toLocalDateTime(),
                end = endDay.toLocalDateTime()
            )
        ),
        dailyCount = HabitTrack.DailyCount(dailyCount),
        comment = comment?.let { HabitTrack.Comment(it) }
    )

}