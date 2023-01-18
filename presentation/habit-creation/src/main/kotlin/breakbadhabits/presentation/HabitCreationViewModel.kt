package breakbadhabits.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HabitCreationViewModel(
    private val habitCreator: HabitCreator,
    private val nameValidator: HabitNewNameValidator,
    private val trackIntervalValidator: HabitTrackIntervalValidator,
    habitIconsProvider: HabitIconsProvider
) : ViewModel() {

    private val icons = habitIconsProvider.provide()
    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val nameState = MutableStateFlow<Habit.Name?>(null)
    private val validatedNameState = nameState.map {
        it?.let { nameValidator.validate(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val selectedIconState = MutableStateFlow(icons.first())
    private val countabilityState = MutableStateFlow<HabitCountability?>(null)
    private val firstTrackRangeState = MutableStateFlow<HabitTrack.Range?>(null)
    private val validatedFirstTrackIntervalState = firstTrackRangeState.map {
        it?.let { trackIntervalValidator.validate(it) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val state = combine(
        creationState,
        nameState,
        validatedNameState,
        selectedIconState,
        countabilityState,
        firstTrackRangeState,
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
                firstTrackRange = firstTrackInterval,
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
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Input(
            name = null,
            validatedName = null,
            icons = emptyList(),
            selectedIcon = icons.first(),
            habitCountability = null,
            firstTrackRange = null,
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

        viewModelScope.launch {
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

    fun updateFirstTrackInterval(range: HabitTrack.Range) {
        require(state.value is State.Input)
        firstTrackRangeState.value = range
    }

    sealed class State {
        data class Input(
            val name: Habit.Name?,
            val validatedName: ValidatedHabitNewName?,
            val icons: List<Habit.IconResource>,
            val selectedIcon: Habit.IconResource,
            val habitCountability: HabitCountability?,
            val firstTrackRange: HabitTrack.Range?,
            val validatedFirstTrackInterval: ValidatedHabitTrackInterval?,
            val creationAllowed: Boolean
        ) : State()

        class Creating : State()
        class Created : State()
    }

    sealed class CreationState {
        class NotExecuted : CreationState()
        class Executing : CreationState()
        class Executed : CreationState()
    }
}
