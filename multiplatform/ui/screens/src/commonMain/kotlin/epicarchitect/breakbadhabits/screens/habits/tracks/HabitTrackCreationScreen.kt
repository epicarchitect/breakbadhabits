package epicarchitect.breakbadhabits.screens.habits.tracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.math.ranges.isStartSameAsEnd
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import epicarchitect.breakbadhabits.foundation.uikit.Icon
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.SingleSelectionChipRow
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RequestButton
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.regex.Regexps
import epicarchitect.breakbadhabits.foundation.uikit.text.ErrorText
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldInputAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedInputField
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedTextField
import epicarchitect.breakbadhabits.logic.datetime.provider.currentDateTimeFlow
import epicarchitect.breakbadhabits.logic.datetime.provider.getCurrentDateTime
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.ui.icons.Icons
import epicarchitect.calendar.compose.datepicker.EpicDatePicker
import epicarchitect.calendar.compose.datepicker.state.EpicDatePickerState
import epicarchitect.calendar.compose.datepicker.state.rememberEpicDatePickerState
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

val LocalHabitTrackCreationResources = compositionLocalOf<HabitTrackCreationResources> {
    error("LocalHabitTrackCreationResources not provided")
}

interface HabitTrackCreationResources {
    val titleText: String
    val commentDescription: String
    val commentLabel: String
    val finishDescription: String
    val finishButton: String
    fun habitNameLabel(habitName: String): String
}

@Composable
fun HabitTrackCreation(viewModel: HabitTrackCreationViewModel) {
    val logicModule = LocalAppModule.current.logic
    val uiModule = LocalAppModule.current.ui
    val currentTime by logicModule.dateTime.dateTimeProvider.currentDateTimeFlow()
        .collectAsState(logicModule.dateTime.dateTimeProvider.getCurrentDateTime())
    val dateTimeFormatter = uiModule.format.dateTimeFormatter
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val rangeState by viewModel.timeInputController.state.collectAsState()
    var selectedTimeSelectionIndex by remember { mutableStateOf(0) }
    val resources = LocalHabitTrackCreationResources.current

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        val state = rememberEpicDatePickerState(
            selectedDates = viewModel.timeInputController.state.value.input.let {
                listOf(it.start.date, it.endInclusive.date)
            },
            selectionMode = EpicDatePickerState.SelectionMode.Range
        )
        Dialog(
            onDismiss = {
                rangeSelectionShow = false
                viewModel.timeInputController.changeInput(
                    state.selectedDates.let {
                        LocalDateTime(
                            it.first(),
                            LocalTime(0, 0)
                        )..LocalDateTime(
                            it.last(),
                            LocalTime(0, 0)
                        )
                    }
                )
            }
        ) {
            EpicDatePicker(
                modifier = Modifier,
                state = state
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.titleText,
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(4.dp))

        LoadingBox(viewModel.habitController) {
            if (it != null) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = resources.habitNameLabel(it.name),
                    type = Text.Type.Description,
                    priority = Text.Priority.Low
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите сколько примерно было событий привычки каждый день"
        )

        Spacer(Modifier.height(12.dp))

        ValidatedInputField(
            modifier = Modifier.padding(horizontal = 16.dp),
            controller = viewModel.eventCountInputController,
            inputAdapter = remember {
                TextFieldInputAdapter(
                    decodeInput = { it.toString() },
                    encodeInput = {
                        it.toIntOrNull() ?: 0
                    }
                )
            },
            validationAdapter = remember {
                TextFieldValidationAdapter {
                    if (it !is IncorrectHabitTrackEventCount) {
                        null
                    } else {
                        when (it.reason) {
                            is IncorrectHabitTrackEventCount.Reason.Empty -> {
                                "Поле не может быть пустым"
                            }
                        }
                    }
                }
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите когда произошло событие:"
        )

        Spacer(Modifier.height(12.dp))

        LaunchedEffect(selectedTimeSelectionIndex) {
            if (selectedTimeSelectionIndex == 0) {
                viewModel.timeInputController.changeInput(
                    currentTime..currentTime
                )
            }

            if (selectedTimeSelectionIndex == 1) {
//                timeInputController.changeInput(
//                    ZonedDateTimeRange.of(
//                        currentTime.minus(1.days).withZeroSeconds()
//                    )
//                )
            }
        }

        SingleSelectionChipRow(
            items = listOf("Сейчас", "Вчера", "Свой интервал"),
            onClick = {
                if (it == 2) {
                    rangeSelectionShow = true
                }
                selectedTimeSelectionIndex = it
            },
            selectedIndex = selectedTimeSelectionIndex
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                rangeSelectionShow = true
            },
            text = rangeState.input.let {
                if (it.isStartSameAsEnd) {
                    val start = dateTimeFormatter.formatDateTime(it.start)
                    "Дата и время: $start"
                } else {
                    val start = dateTimeFormatter.formatDateTime(it.start)
                    val end = dateTimeFormatter.formatDateTime(it.endInclusive)
                    "Первое событие: $start, последнее событие: $end"
                }
            }
        )

        (rangeState.validationResult as? IncorrectHabitTrackDateTimeRange)?.let {
            Spacer(Modifier.height(8.dp))
            when (it.reason) {
                IncorrectHabitTrackDateTimeRange.Reason.BiggestThenCurrentTime -> {
                    ErrorText(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "Нельзя выбрать время больше чем текущее"
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = resources.commentDescription
        )

        Spacer(Modifier.height(12.dp))

        ValidatedTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = resources.commentLabel,
            controller = viewModel.commentInputController,
            validationAdapter = remember {
                TextFieldValidationAdapter {
                    null
                }
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = resources.finishDescription
        )

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            controller = viewModel.creationController,
            text = resources.finishButton,
            type = Button.Type.Main,
            icon = {
                Icon(Icons.Done)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}