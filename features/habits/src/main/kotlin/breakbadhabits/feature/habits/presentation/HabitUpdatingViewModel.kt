package breakbadhabits.feature.habits.presentation


import breakbadhabits.entity.Habit
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitUpdatingViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitNewNameValidator: HabitNewNameValidator,
    private val id: Habit.Id
) {

    private val initialHabitLoadState = MutableStateFlow<InitialHabitLoadState?>(null)
    private val updatingState = MutableStateFlow<UpdatingState>(UpdatingState.NotExecuted())
    private val nameState = MutableStateFlow<Habit.Name?>(null)
    private val iconResourceState = MutableStateFlow<Habit.IconResource?>(null)

    val state = combine(
        initialHabitLoadState,
        updatingState,
        nameState.filterNotNull(),
        nameState.filterNotNull().map { habitNewNameValidator.validate(it) },
        iconResourceState.filterNotNull()
    ) { initialHabitLoadState, updatingState, name, validatedName, iconResource ->
        when (initialHabitLoadState) {
            is InitialHabitLoadState.Loaded -> {
                when (updatingState) {
                    is UpdatingState.NotExecuted -> {
                        State.Input(
                            name = name,
                            validatedName = validatedName,
                            iconResource = iconResource,
                            updatingAllowed = validatedName is Correct
                                    && (name != initialHabitLoadState.habit.name
                                    || iconResource != initialHabitLoadState.habit.iconResource)
                        )
                    }

                    is UpdatingState.Executed -> {
                        State.Updated()
                    }

                    is UpdatingState.Executing -> {
                        State.Updating()
                    }
                }
            }

            is InitialHabitLoadState.NotExist -> {
                error("Nice, i dont wont fix this right now, ok yes?")
            }

            else -> {
                State.InitialHabitLoading()
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State.InitialHabitLoading()
    )

    init {
        loadInitialHabit()
    }

    fun startCreation() {
        val state = state.value

        require(state is State.Input)
        require(state.validatedName is Correct)
        requireNotNull(state.updatingAllowed)

        updatingState.value = UpdatingState.Executing()

        coroutineScope.launch {
            habitsRepository.updateHabit(
                id = id,
                name = state.validatedName,
                iconResource = state.iconResource
            )

            updatingState.value = UpdatingState.Executed()
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

    fun dispose() {
        coroutineScope.cancel()
    }

    private fun loadInitialHabit() {
        initialHabitLoadState.value = InitialHabitLoadState.Loading()
        coroutineScope.launch {
            val habit = habitsRepository.getHabitById(id)
            if (habit == null) {
                initialHabitLoadState.value = InitialHabitLoadState.NotExist()
            } else {
                nameState.value = habit.name
                iconResourceState.value = habit.iconResource
                initialHabitLoadState.value = InitialHabitLoadState.Loaded(habit)
            }
        }
    }

    sealed class State {
        class InitialHabitLoading : State()
        data class Input(
            val name: Habit.Name,
            val validatedName: Validated<Habit.Name, HabitNewNameValidator.IncorrectReason>,
            val iconResource: Habit.IconResource,
            val updatingAllowed: Boolean
        ) : State()

        class Updating : State()
        class Updated : State()
    }

    private sealed class UpdatingState {
        class NotExecuted : UpdatingState()
        class Executing : UpdatingState()
        class Executed : UpdatingState()
    }

    private sealed class InitialHabitLoadState {
        class NotExist : InitialHabitLoadState()
        class Loading : InitialHabitLoadState()
        class Loaded(val habit: Habit) : InitialHabitLoadState()
    }
}