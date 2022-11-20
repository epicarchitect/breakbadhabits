package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.combine
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.HabitsRepository
import breakbadhabits.feature.habits.validator.HabitNewNameValidator
import breakbadhabits.feature.habits.validator.HabitTrackIntervalValidator
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.alexander.validation.Validated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitCreationViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
    private val habitNewNameValidator: HabitNewNameValidator,
    private val habitTrackIntervalValidator: HabitTrackIntervalValidator
) {
    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val nameState = MutableStateFlow<Habit.Name?>(null)
    private val iconResourceState = MutableStateFlow<Habit.IconResource?>(null)
    private val countabilityState = MutableStateFlow<HabitCountability?>(null)
    private val firstTrackIntervalState = MutableStateFlow<HabitTrack.Interval?>(null)

    val state = combine(
        creationState,
        nameState,
        nameState.map { it?.let { habitNewNameValidator.validate(it) } },
        iconResourceState,
        countabilityState,
        firstTrackIntervalState,
        firstTrackIntervalState.map { it?.let { habitTrackIntervalValidator.validate(it) } },
    ) { creationState, name, validatedName, iconResource,
        countability, firstTrackInterval, validatedFirstTrackInterval ->
        when (creationState) {
            is CreationState.NotExecuted -> State.Input(
                name = name,
                validatedName = validatedName,
                iconResource = iconResource,
                countability = countability,
                firstTrackInterval = firstTrackInterval,
                validatedFirstTrackInterval = validatedFirstTrackInterval,
                creationAllowed = name != null
                        && validatedName is Correct
                        && iconResource != null
                        && countability != null
                        && firstTrackInterval != null
                        && validatedFirstTrackInterval is Correct
            )

            is CreationState.Executing -> State.Creating()
            is CreationState.Executed -> State.Created()
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.Input(
            name = null,
            validatedName = null,
            iconResource = null,
            countability = null,
            firstTrackInterval = null,
            validatedFirstTrackInterval = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value

        require(state is State.Input)
        require(state.creationAllowed)
        require(state.validatedName is Correct)
        requireNotNull(state.iconResource)
        requireNotNull(state.countability)
        requireNotNull(state.firstTrackInterval)
        require(state.validatedFirstTrackInterval is Correct)

        creationState.value = CreationState.Executing()

        coroutineScope.launch {
            val newHabit = habitsRepository.insertHabit(
                name = state.validatedName,
                iconResource = state.iconResource,
                countability = Habit.Countability(
                    idCountable = state.countability is HabitCountability.Countable
                )
            )

            habitTracksRepository.insertHabitTrack(
                habitId = newHabit.id,
                interval = state.validatedFirstTrackInterval,
                dailyCount = when (state.countability) {
                    is HabitCountability.Countable -> state.countability.averageDailyCount
                    is HabitCountability.Uncountable -> HabitTrack.DailyCount(100)
                },
                comment = null
            )

            creationState.value = CreationState.Executed()
        }
    }

    fun updateName(name: Habit.Name) {
        require(state.value is State.Input)
        nameState.value = name
    }

    fun updateIconResource(iconId: Habit.IconResource) {
        require(state.value is State.Input)
        iconResourceState.value = iconId
    }

    fun updateCountably(countability: HabitCountability) {
        require(state.value is State.Input)
        countabilityState.value = countability
    }

    fun updateFirstTrackInterval(interval: HabitTrack.Interval) {
        require(state.value is State.Input)
        firstTrackIntervalState.value = interval
    }

    fun dispose() {
        coroutineScope.cancel()
    }

    sealed class State {
        data class Input(
            val name: Habit.Name?,
            val validatedName: Validated<Habit.Name, HabitNewNameValidator.IncorrectReason>?,
            val iconResource: Habit.IconResource?,
            val countability: HabitCountability?,
            val firstTrackInterval: HabitTrack.Interval?,
            val validatedFirstTrackInterval: Validated<HabitTrack.Interval, HabitTrackIntervalValidator.IncorrectReason>?,
            val creationAllowed: Boolean
        ) : State()

        class Creating : State()
        class Created : State()
    }

    sealed class HabitCountability {
        class Countable(val averageDailyCount: HabitTrack.DailyCount) : HabitCountability()
        class Uncountable : HabitCountability()
    }

    private sealed class CreationState {
        class NotExecuted : CreationState()
        class Executing : CreationState()
        class Executed : CreationState()
    }
}