package breakbadhabits.logic

import breakbadhabits.entity.Habit

class HabitDeleter internal constructor(
    private val delegate: HabitDeleterModule.Delegate
) {

    suspend fun deleteById(id: Habit.Id) {
        delegate.deleteHabitById(id)
        delegate.deleteHabitTracksByHabitId(id)
    }
}