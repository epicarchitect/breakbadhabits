package breakbadhabits.logic

class HabitIconsProvider internal constructor(
    private val delegate: HabitIconsProviderModule.Delegate
) {

    fun provide() = delegate.getHabitIcons()

}