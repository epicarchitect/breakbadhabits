package breakbadhabits.feature.habits.di

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.HabitsRepository
import breakbadhabits.feature.habits.model.TimeProvider
import breakbadhabits.feature.habits.presentation.CurrentHabitAbstinenceViewModel
import breakbadhabits.feature.habitCreation.HabitCreationViewModel
import breakbadhabits.feature.habits.presentation.HabitDeletionViewModel
import breakbadhabits.feature.habits.presentation.HabitIdsViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackCreationViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackUpdatingViewModel
import breakbadhabits.feature.habits.presentation.HabitTrackViewModel
import breakbadhabits.feature.habitUpdating.HabitUpdatingViewModel
import breakbadhabits.feature.habits.presentation.HabitViewModel
import breakbadhabits.feature.habits.validator.HabitNewNameValidator
import breakbadhabits.feature.habits.validator.HabitTrackIntervalValidator

class HabitsFeatureFactory(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider,
    private val maxHabitNameLength: Int
) {

    fun createHabitCreationViewModel() = HabitCreationViewModel(
        habitsRepository,
        habitTracksRepository,
        HabitNewNameValidator(maxHabitNameLength, habitsRepository),
        HabitTrackIntervalValidator(timeProvider)
    )

    fun createHabitDeletionViewModel(id: Habit.Id) = HabitDeletionViewModel(
        habitsRepository,
        habitTracksRepository,
        id
    )

    fun createHabitUpdatingViewModel(id: Habit.Id) = HabitUpdatingViewModel(
        habitsRepository,
        HabitNewNameValidator(maxHabitNameLength, habitsRepository),
        id
    )

    fun createHabitViewModel(id: Habit.Id) = HabitViewModel(
        habitsRepository,
        id
    )

    fun createHabitCurrentAbstinenceViewModel(id: Habit.Id) = CurrentHabitAbstinenceViewModel(
        habitTracksRepository,
        timeProvider,
        id
    )

    fun createHabitIdsViewModel() = HabitIdsViewModel(
        habitsRepository,
    )

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = HabitTrackCreationViewModel(
        habitTracksRepository,
        HabitTrackIntervalValidator(timeProvider),
        habitId
    )

    fun createHabitTrackUpdatingViewModel(id: HabitTrack.Id) = HabitTrackUpdatingViewModel(
        habitTracksRepository,
        HabitTrackIntervalValidator(timeProvider),
        id
    )

    fun createHabitTrackViewModel(id: HabitTrack.Id) = HabitTrackViewModel(
        habitTracksRepository,
        id
    )
}