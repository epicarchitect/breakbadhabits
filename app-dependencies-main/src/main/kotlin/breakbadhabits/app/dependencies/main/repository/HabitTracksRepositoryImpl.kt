package breakbadhabits.app.dependencies.main.repository

import breakbadhabits.app.dependencies.main.database.HabitTracksDao
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.mapItems
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.feature.habits.model.HabitTracksRepository
import kolmachikhin.alexander.validation.Correct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import breakbadhabits.app.dependencies.main.database.HabitTrack as DatabaseHabitTrack

internal class HabitTracksRepositoryImpl(
    private val idGenerator: IdGenerator,
    private val habitTracksDao: HabitTracksDao
) : HabitTracksRepository {
    override suspend fun insertHabitTrack(
        habitId: Habit.Id,
        interval: Correct<HabitTrack.Interval>,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) = withContext(Dispatchers.IO) {
        habitTracksDao.insert(
            HabitTrack(
                HabitTrack.Id(idGenerator.nextId()),
                habitId,
                interval.data,
                dailyCount,
                comment
            ).toDatabaseHabitTrack()
        )
    }

    override suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        interval: Correct<HabitTrack.Interval>,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?
    ) = withContext(Dispatchers.IO) {
        habitTracksDao.update(
            id = id.value,
            startDay = interval.data.value.start.toString(),
            endDay = interval.data.value.start.toString(),
            dailyCount = dailyCount.value,
            comment = comment?.value
        )
    }

    override suspend fun deleteHabitTracksByHabitId(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.deleteEntitiesByHabitId(id.value)
    }

    override suspend fun deleteHabitTrackById(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.deleteEntityById(id.value)
    }

    override suspend fun getHabitTrackById(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        habitTracksDao.getEntityById(id.value)?.toHabitTrack()
    }

    override fun habitTrackFlowById(id: HabitTrack.Id) =
        habitTracksDao.entityByIdFlow(id.value).map { it?.toHabitTrack() }

    override fun habitTracksFlowByHabitId(id: Habit.Id) =
        habitTracksDao.entityListByHabitIdFlow(id.value).mapItems { it.toHabitTrack() }

    override fun habitTrackFlowByHabitIdAndLastByTime(id: Habit.Id) =
        habitTracksDao.entityListByHabitIdFlow(id.value).map { tracks ->
            tracks.maxByOrNull {
                it.endDay.toLocalDateTime()
            }?.toHabitTrack()
        }

    override fun habitTracksFlow() = habitTracksDao.entityListFlow().mapItems { it.toHabitTrack() }

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