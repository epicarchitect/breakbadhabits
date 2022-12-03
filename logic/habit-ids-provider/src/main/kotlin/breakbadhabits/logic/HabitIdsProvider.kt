package breakbadhabits.logic

class HabitIdsProvider internal constructor(
    private val delegate: HabitIdsProviderModule.Delegate
) {

    fun provideFlow() = delegate.habitIdsFlow()

}