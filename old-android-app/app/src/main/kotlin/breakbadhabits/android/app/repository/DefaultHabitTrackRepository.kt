package breakbadhabits.android.app.repository

import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.mapItems
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DefaultHabitTrackRepository(
    private val idGenerator: IdGenerator,
    mainDatabase: MainDatabase
) : HabitTracksRepository {

    private val habitDao = mainDatabase.habitDao
    private val habitTracksDao = mainDatabase.habitTracksDao

    override suspend fun saveHabitTrack(track: HabitTrack) =
        habitTracksDao.saveEntity(track.toDatabaseEntity())

    override suspend fun deleteHabitTracksByHabitId(habitId: Int) =
        habitTracksDao.deleteEntitiesByHabitId(habitId)

    override suspend fun deleteHabitTracksById(id: Int) =
        habitTracksDao.deleteEntityById(id)

    override fun habitTracksByIdFlow(id: Int) =
        habitTracksDao.entityByIdFlow(id).map { it?.toEntity() }

    override fun habitTracksByHabitIdFlow(habitId: Int) =
        habitTracksDao.entityListByHabitIdFlow(habitId).mapItems { it.toEntity() }

    override fun lastByTimeHabitEventByHabitIdFlow(habitId: Int) =
        habitTracksByHabitIdFlow(habitId).map {
            it.maxByOrNull { it.endDay }
        }

    override fun habitTracksListFlow() =
        habitTracksDao.entityListFlow().mapItems { it.toEntity() }

    override suspend fun lastByTimeHabitTrackByHabitId(habitId: Int) =
        lastByTimeHabitEventByHabitIdFlow(habitId).first()


    override fun generateNextId() = idGenerator.nextId()
}