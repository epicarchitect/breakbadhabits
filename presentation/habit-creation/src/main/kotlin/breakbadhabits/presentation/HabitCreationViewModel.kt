package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.combine
import breakbadhabits.logic.CorrectHabitNewNewName
import breakbadhabits.logic.CorrectHabitTrackInterval
import breakbadhabits.logic.HabitCountability
import breakbadhabits.logic.HabitCreator
import breakbadhabits.logic.HabitIconsProvider
import breakbadhabits.logic.HabitNewNameValidator
import breakbadhabits.logic.HabitTrackIntervalValidator
import breakbadhabits.logic.ValidatedHabitNewName
import breakbadhabits.logic.ValidatedHabitTrackInterval
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitCreationViewModel internal constructor(
    private val habitCreator: HabitCreator,
    private val nameValidator: HabitNewNameValidator,
    private val trackIntervalValidator: HabitTrackIntervalValidator,
    habitIconsProvider: HabitIconsProvider
) : EpicViewModel() {

    private val icons = habitIconsProvider.provide()
    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val nameState = MutableStateFlow<Habit.Name?>(null)
    private val validatedNameState: StateFlow<ValidatedHabitNewName?> = nameState.map {
        it?.let { nameValidator.validate(it) }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
    private val selectedIconState = MutableStateFlow(icons.first())
    private val countabilityState = MutableStateFlow<HabitCountability?>(null)
    private val firstTrackIntervalState = MutableStateFlow<HabitTrack.Interval?>(null)
    private val validatedFirstTrackIntervalState: StateFlow<ValidatedHabitTrackInterval?> =
        firstTrackIntervalState.map {
            it?.let { trackIntervalValidator.validate(it) }
        }.stateIn(coroutineScope, SharingStarted.Eagerly, null)


    val state = combine(
        creationState,
        nameState,
        validatedNameState,
        selectedIconState,
        countabilityState,
        firstTrackIntervalState,
        validatedFirstTrackIntervalState
    ) { creationState, name, validatedName, selectedIcon,
        countability, firstTrackInterval, validatedFirstTrackInterval ->
        when (creationState) {
            is CreationState.NotExecuted -> State.Input(
                name = name,
                validatedName = validatedName,
                selectedIcon = selectedIcon,
                icons = icons,
                habitCountability = countability,
                firstTrackInterval = firstTrackInterval,
                validatedFirstTrackInterval = validatedFirstTrackInterval,
                creationAllowed = name != null
                        && validatedName is CorrectHabitNewNewName
                        && countability != null
                        && firstTrackInterval != null
                        && validatedFirstTrackInterval is CorrectHabitTrackInterval
            )

            is CreationState.Executing -> State.Creating()
            is CreationState.Executed -> State.Created()
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Input(
            name = null,
            validatedName = null,
            icons = emptyList(),
            selectedIcon = icons.first(),
            habitCountability = null,
            firstTrackInterval = null,
            validatedFirstTrackInterval = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value
        require(state is State.Input)
        require(state.creationAllowed)

        val validatedName = validatedNameState.value
        require(validatedName is CorrectHabitNewNewName)

        val selectedIcon = selectedIconState.value

        val countability = countabilityState.value
        requireNotNull(countability)

        val validatedFirstTrackInterval = validatedFirstTrackIntervalState.value
        require(validatedFirstTrackInterval is CorrectHabitTrackInterval)

        creationState.value = CreationState.Executing()

        coroutineScope.launch {
            habitCreator.createHabit(
                validatedName,
                selectedIcon,
                countability,
                validatedFirstTrackInterval
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
        selectedIconState.value = iconId
    }

    fun updateCountably(countability: HabitCountability) {
        require(state.value is State.Input)
        countabilityState.value = countability
    }

    fun updateFirstTrackInterval(interval: HabitTrack.Interval) {
        require(state.value is State.Input)
        firstTrackIntervalState.value = interval
    }

    sealed class State {
        data class Input(
            val name: Habit.Name?,
            val validatedName: ValidatedHabitNewName?,
            val icons: List<Habit.IconResource>,
            val selectedIcon: Habit.IconResource,
            val habitCountability: HabitCountability?,
            val firstTrackInterval: HabitTrack.Interval?,
            val validatedFirstTrackInterval: ValidatedHabitTrackInterval?,
            val creationAllowed: Boolean
        ) : State()

        class Creating : State()
        class Created : State()
    }

    private sealed class CreationState {
        class NotExecuted : CreationState()
        class Executing : CreationState()
        class Executed : CreationState()
    }
}