package breakbadhabits.app.presentation.habit.creation

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habit.creator.CorrectHabitNewNewName
import breakbadhabits.app.logic.habit.creator.CorrectHabitTrackRange
import breakbadhabits.app.logic.habit.creator.HabitCountability
import breakbadhabits.app.logic.habit.creator.HabitCreator
import breakbadhabits.app.logic.habit.creator.HabitNewNameValidator
import breakbadhabits.app.logic.habit.creator.HabitTrackIntervalValidator
import breakbadhabits.app.logic.habit.creator.ValidatedHabitTrackInterval
import breakbadhabits.app.logic.habit.icon.provider.HabitIconProvider
import breakbadhabits.framework.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitCreationViewModel(
    private val habitCreator: HabitCreator,
    private val nameValidator: HabitNewNameValidator,
    private val trackIntervalValidator: HabitTrackIntervalValidator,
    habitIconProvider: HabitIconProvider
) : ViewModel() {

    val habitIconSelectionController = SingleSelectionController(
        coroutineScope = viewModelScope,
        items = habitIconProvider.provide(),
        default = List<Habit.IconResource>::first
    )

    val habitNameController = ValidatedInputController(
        initialInput = Habit.Name(""),
        validate = nameValidator::validate,
        coroutineScope = viewModelScope
    )

    val habitCountabilityController = ValidatedInputController<HabitCountability?, Unit>(
        initialInput = null,
        validate = {},
        coroutineScope = viewModelScope
    )

    val firstTrackRangeInputController =
        ValidatedInputController<HabitTrack.Range?, ValidatedHabitTrackInterval>(
            initialInput = null,
            validate = {
                if (it == null) null
                else trackIntervalValidator.validate(it)
            },
            coroutineScope = viewModelScope
        )

    val creationController = RequestController(
        request = {
            val habitIcon = habitIconSelectionController.state.value.selectedItem
            val validatedHabitName = habitNameController.state.value.validationResult
            val validatedHabitCountability = habitCountabilityController.state.value.input
            val validatedFirstTrackRange = firstTrackRangeInputController.state.value.validationResult

            require(validatedHabitName is CorrectHabitNewNewName)
            require(validatedFirstTrackRange is CorrectHabitTrackRange)
            requireNotNull(validatedHabitCountability)

            habitCreator.createHabit(
                validatedHabitName,
                habitIcon,
                validatedHabitCountability,
                validatedFirstTrackRange
            )
        },
        coroutineScope = viewModelScope
    )
}

class SingleSelectionController<T>(
    coroutineScope: CoroutineScope,
    items: List<T>,
    default: (List<T>) -> T
) {
    private val selected = MutableStateFlow(default(items))

    val state = selected.map {
        State(items, it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State(items, default(items))
    )

    fun select(item: T) {
        selected.value = item
    }

    class State<T>(
        val items: List<T>,
        val selectedItem: T
    )
}

class RequestController(
    request: suspend () -> Unit,
    private val coroutineScope: CoroutineScope
) {
    private val internalRequest = request

    private val mutableState = MutableStateFlow<State>(State.NotExecuted())
    val state = mutableState.asStateFlow()

    fun request() {
        coroutineScope.launch {
            mutableState.value = State.Executing()
            internalRequest()
            mutableState.value = State.Executed()
        }
    }

    sealed class State {
        class NotExecuted : State()
        class Executing : State()
        class Executed : State()
    }
}

class ValidatedInputController<INPUT, VALIDATION_RESULT>(
    coroutineScope: CoroutineScope,
    initialInput: INPUT,
    validate: suspend (INPUT) -> VALIDATION_RESULT?
) {
    private val inputState = MutableStateFlow(initialInput)
    private val validationResultState = inputState.map(validate).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    val state = combine(
        inputState,
        validationResultState
    ) { input, result ->
        State(input, result)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State(initialInput, null)
    )

    fun changeInput(value: INPUT) {
        inputState.value = value
    }

    class State<INPUT, VALIDATION_RESULT>(
        val input: INPUT,
        val validationResult: VALIDATION_RESULT
    )
}