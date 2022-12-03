package breakbadhabits.logic

import breakbadhabits.entity.Habit

class HabitProvider internal constructor(
    private val delegate: HabitProviderModule.Delegate
) {

    fun provideFlow(id: Habit.Id) = delegate.habitFlow(id)

    suspend fun provide(id: Habit.Id) = delegate.getHabit(id)

}