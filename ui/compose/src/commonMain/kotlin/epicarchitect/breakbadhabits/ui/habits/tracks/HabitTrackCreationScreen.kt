package epicarchitect.breakbadhabits.ui.habits.tracks

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange
import epicarchitect.breakbadhabits.foundation.datetime.minus
import epicarchitect.breakbadhabits.foundation.datetime.withZeroSeconds
import epicarchitect.breakbadhabits.foundation.math.ranges.isStartSameAsEnd
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
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackTime
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackTime
import kotlin.time.Duration.Companion.days

@Composable
fun HabitTrackCreation(
    eventCountInputController: ValidatedInputController<Int, ValidatedHabitTrackEventCount>,
    timeInputController: ValidatedInputController<ZonedDateTimeRange, ValidatedHabitTrackTime>,
    creationController: SingleRequestController,
    habitController: LoadingController<Habit?>,
    commentInputController: ValidatedInputController<String, Nothing>
) {
    val logicModule = LocalAppModule.current.logic
    val uiModule = LocalAppModule.current.ui
    val currentTime by logicModule.dateTime.dateTimeProvider.currentDateTimeFlow()
        .collectAsState(logicModule.dateTime.dateTimeProvider.getCurrentDateTime())
    val dateTimeFormatter = uiModule.format.dateTimeFormatter
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val rangeState by timeInputController.state.collectAsState()
    var selectedTimeSelectionIndex by remember { mutableStateOf(0) }

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
//        val epicCalendarState = rememberSelectionEpicCalendarState(
//            timeZone = timeZone,
//            initialRange = rangeState.input
//        )
//
//        SelectionEpicCalendarDialog(
//            state = epicCalendarState,
//            onSelected = {
//                rangeSelectionShow = false
//                selectedTimeSelectionIndex = 2
//                timeInputController.changeInput(
//                    it.withZeroSeconds(timeZone)
//                )
//            },
//            onCancel = {
//                rangeSelectionShow = false
//            }
//        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "stringResource(R.string.habitEventCreation_title)",
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(4.dp))

        LoadingBox(habitController) {
            if (it != null) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
//                    text = stringResource(
//                        R.string.habitEventCreation_habitName,
//                        it.name
//                    ),
                    text = it.name,
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
            controller = eventCountInputController,
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
                    if (it !is IncorrectHabitTrackEventCount) null
                    else when (it.reason) {
                        is IncorrectHabitTrackEventCount.Reason.Empty -> {
                            "Поле не может быть пустым"
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
                timeInputController.changeInput(
                    ZonedDateTimeRange.of(
                        currentTime.withZeroSeconds()
                    )
                )
            }

            if (selectedTimeSelectionIndex == 1) {
                timeInputController.changeInput(
                    ZonedDateTimeRange.of(
                        currentTime.minus(1.days).withZeroSeconds()
                    )
                )
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
                    val start = dateTimeFormatter.formatDateTime(it.start.instant)
                    "Дата и время: $start"
                } else {
                    val start = dateTimeFormatter.formatDateTime(it.start.instant)
                    val end = dateTimeFormatter.formatDateTime(it.endInclusive.instant)
                    "Первое событие: $start, последнее событие: $end"
                }
            }
        )

        (rangeState.validationResult as? IncorrectHabitTrackTime)?.let {
            Spacer(Modifier.height(8.dp))
            when (it.reason) {
                IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime -> {
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
            text = "stringResource(R.string.habitEventCreation_comment_description)"
        )


        Spacer(Modifier.height(12.dp))

        ValidatedTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = "stringResource(R.string.habitEventCreation_comment)",
            controller = commentInputController,
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
            text = "stringResource(R.string.habitEventCreation_finish_description)"
        )

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            controller = creationController,
            text = "stringResource(R.string.habitEventCreation_finish)",
            type = Button.Type.Main,
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_done)
//            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}