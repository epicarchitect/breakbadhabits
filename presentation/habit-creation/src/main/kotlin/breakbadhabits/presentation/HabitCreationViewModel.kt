package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.combine
import breakbadhabits.logic.HabitCreator
import breakbadhabits.logic.HabitIconsProvider
import breakbadhabits.logic.HabitNewNameValidator
import breakbadhabits.logic.HabitTrackIntervalValidator
import kolmachikhin.alexander.validation.Correct
import kolmachikhin.alexander.validation.Incorrect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitCreationViewModel(
    private val habitNewNameValidator: HabitNewNameValidator,
    private val habitTrackIntervalValidator: HabitTrackIntervalValidator,
    private val habitTrackIntervalFormatter: HabitTrackIntervalFormatter,
    private val habitIconsProvider: HabitIconsProvider,
    private val habitCreator: HabitCreator
) : EpicViewModel() {

    private val creationState = MutableStateFlow<CreationState>(CreationState.NotExecuted())
    private val nameState = MutableStateFlow<Habit.Name?>(null)
    private val validatedNameState = nameState.map {
        it?.let { habitNewNameValidator.validate(it) }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)
    private val selectedIconState = MutableStateFlow<Habit.IconResource?>(null)
    private val countabilityState = MutableStateFlow<HabitCreator.HabitCountability?>(null)
    private val firstTrackIntervalState = MutableStateFlow<HabitTrack.Interval?>(null)
    private val validatedFirstTrackIntervalState = firstTrackIntervalState.map {
        it?.let { habitTrackIntervalValidator.validate(it) }
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val state = combine(
        creationState,
        nameState,
        validatedNameState,
        selectedIconState,
        countabilityState,
        firstTrackIntervalState,
        validatedFirstTrackIntervalState,
        flowOf(habitIconsProvider.provide())
    ) { creationState, name, validatedName, selectedIcon,
        countability, firstTrackInterval, validatedFirstTrackInterval, icons ->
        when (creationState) {
            is CreationState.NotExecuted -> State.Input(
                name = name?.value ?: "",
                nameValidationError = when (validatedName) {
                    is Incorrect -> when (validatedName.reason) {
                        is HabitNewNameValidator.IncorrectReason.AlreadyUsed -> "Already Used"
                        is HabitNewNameValidator.IncorrectReason.Empty -> "Empty"
                        is HabitNewNameValidator.IncorrectReason.TooLong -> "Too long"
                    }

                    else -> null
                },
                selectedIcon = selectedIcon ?: icons.first(),
                icons = icons,
                habitCountability = when (countability) {
                    is HabitCreator.HabitCountability.Countable -> {
                        HabitCountability.Countable(countability.averageDailyCount.value.toString())
                    }

                    is HabitCreator.HabitCountability.Uncountable -> {
                        HabitCountability.Uncountable()
                    }

                    null -> null
                },
                formattedFirstTrackInterval = firstTrackInterval?.let {
                    habitTrackIntervalFormatter.format(it)
                },
                firstTrackIntervalValidationError = null,
                creationAllowed = name != null
                        && validatedName is Correct
                        && selectedIcon != null
                        && countability != null
                        && firstTrackInterval != null
                        && validatedFirstTrackInterval is Correct
            )

            is CreationState.Executing -> State.Creating()
            is CreationState.Executed -> State.Created()
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Input(
            name = "",
            nameValidationError = null,
            icons = emptyList(),
            selectedIcon = null,
            habitCountability = null,
            formattedFirstTrackInterval = null,
            firstTrackIntervalValidationError = null,
            creationAllowed = false
        )
    )

    fun startCreation() {
        val state = state.value
        require(state is State.Input)
        require(state.creationAllowed)

        val validatedName = validatedNameState.value
        require(validatedName is Correct)

        val selectedIcon = selectedIconState.value
        requireNotNull(selectedIcon)

        val countability = countabilityState.value
        requireNotNull(countability)

        val validatedFirstTrackInterval = validatedFirstTrackIntervalState.value
        require(validatedFirstTrackInterval is Correct)

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

    fun updateName(name: String) {
        require(state.value is State.Input)
        nameState.value = Habit.Name(name)
    }

    fun updateIconResource(iconId: Habit.IconResource) {
        require(state.value is State.Input)
        selectedIconState.value = iconId
    }

    fun updateCountably(countability: HabitCountability) {
        require(state.value is State.Input)
        countabilityState.value = null // TODO
    }

    fun updateFirstTrackInterval(interval: HabitTrack.Interval) {
        require(state.value is State.Input)
        firstTrackIntervalState.value = interval
    }

    sealed class State {
        data class Input(
            val name: String,
            val nameValidationError: String?,
            val icons: List<Habit.IconResource>,
            val selectedIcon: Habit.IconResource,
            val habitCountability: HabitCountability?,
            val formattedFirstTrackInterval: FormattedHabitTrackInterval?,
            val firstTrackIntervalValidationError: String?,
            val creationAllowed: Boolean
        ) : State()

        class Creating : State()
        class Created : State()
    }

    sealed class HabitCountability {
        class Countable(val formattedDailyCount: String) : HabitCountability()
        class Uncountable : HabitCountability()
    }

    private sealed class CreationState {
        class NotExecuted : CreationState()
        class Executing : CreationState()
        class Executed : CreationState()
    }
}