package breakbadhabits.feature.habits.di

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.HabitsRepository
import breakbadhabits.feature.habits.model.TimeProvider
import breakbadhabits.feature.habits.presentation.CurrentHabitAbstinenceViewModel
import breakbadhabits.feature.habits.presentation.HabitCreationViewModel
import breakbadhabits.feature.habits.presentation.HabitDeletionViewModel
import breakbadhabits.feature.habits.presentation.HabitIdsViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackCreationViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackUpdatingViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackViewModel
import breakbadhabits.feature.habits.presentation.HabitUpdatingViewModel
import breakbadhabits.feature.habits.presentation.HabitViewModel
import breakbadhabits.feature.habits.validator.HabitNewNameValidator
import breakbadhabits.feature.habits.validator.HabitTrackIntervalValidator
import kotlinx.coroutines.CoroutineScope

class HabitsFeatureFactory(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider,
    private val maxHabitNameLength: Int,
    private val createFeatureCoroutineScope: () -> CoroutineScope
) {

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        createFeatureCoroutineScope(),
        habitsRepository,
        habitTracksRepository,
        HabitNewNameValidator(maxHabitNameLength, habitsRepository),
        HabitTrackIntervalValidator(timeProvider)
    )

    fun createHabitDeletionViewModel(id: Habit.Id) = HabitDeletionViewModel(
        createFeatureCoroutineScope(),
        habitsRepository,
        habitTracksRepository,
        id
    )

    fun createHabitUpdatingViewModel(id: Habit.Id) = HabitUpdatingViewModel(
        createFeatureCoroutineScope(),
        habitsRepository,
        HabitNewNameValidator(maxHabitNameLength, habitsRepository),
        id
    )

    fun createHabitViewModel(id: Habit.Id) = HabitViewModel(
        createFeatureCoroutineScope(),
        habitsRepository,
        id
    )

    fun createHabitCurrentAbstinenceViewModel(id: Habit.Id) = CurrentHabitAbstinenceViewModel(
        createFeatureCoroutineScope(),
        habitTracksRepository,
        timeProvider,
        id
    )

    fun createHabitIdsViewModel() = HabitIdsViewModel(
        createFeatureCoroutineScope(),
        habitsRepository,
    )

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = HabitTrackCreationViewModel(
        createFeatureCoroutineScope(),
        habitTracksRepository,
        HabitTrackIntervalValidator(timeProvider),
        habitId
    )

    fun createHabitTrackUpdatingViewModel(id: HabitTrack.Id) = HabitTrackUpdatingViewModel(
        createFeatureCoroutineScope(),
        habitTracksRepository,
        HabitTrackIntervalValidator(timeProvider),
        id
    )

    fun createHabitTrackViewModel(id: HabitTrack.Id) = HabitTrackViewModel(
        createFeatureCoroutineScope(),
        habitTracksRepository,
        id
    )
}