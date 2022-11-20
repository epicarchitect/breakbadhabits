package breakbadhabits.android.app.repository

import breakbadhabits.entity.HabitTrack
import kotlinx.coroutines.flow.Flow

interface HabitTracksRepository {

    suspend fun saveHabitTrack(track: HabitTrack)

    suspend fun deleteHabitTracksByHabitId(habitId: Int)

    suspend fun deleteHabitTracksById(id: Int)

    fun habitTracksByIdFlow(id: Int): Flow<HabitTrack?>

    fun habitTracksByHabitIdFlow(habitId: Int): Flow<List<HabitTrack>>

    fun lastByTimeHabitEventByHabitIdFlow(habitId: Int): Flow<HabitTrack?>

    fun habitTracksListFlow(): Flow<List<HabitTrack>>

    suspend fun lastByTimeHabitTrackByHabitId(habitId: Int): HabitTrack?

    suspend fun generateNextId(): Int
}