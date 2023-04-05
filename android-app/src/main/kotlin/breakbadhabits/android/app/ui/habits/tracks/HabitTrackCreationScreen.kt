package breakbadhabits.android.app.ui.habits.tracks

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.di.LocalLogicModule
import breakbadhabits.android.app.di.LocalUiModule
import breakbadhabits.app.logic.habits.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.IncorrectHabitTrackTime
import breakbadhabits.app.logic.habits.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.ValidatedHabitTrackTime
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.withZeroSeconds
import breakbadhabits.foundation.math.ranges.asRangeOfOne
import breakbadhabits.foundation.math.ranges.isStartSameAsEnd
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.SingleSelectionChipRow
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.calendar.SelectionEpicCalendarDialog
import breakbadhabits.foundation.uikit.calendar.rememberSelectionEpicCalendarState
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.regex.Regexps
import breakbadhabits.foundation.uikit.text.ErrorText
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldInputAdapter
import breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import breakbadhabits.foundation.uikit.text.ValidatedInputField
import breakbadhabits.foundation.uikit.text.ValidatedTextField
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

@Composable
fun HabitTrackCreationScreen(
    eventCountInputController: ValidatedInputController<Int, ValidatedHabitTrackEventCount>,
    timeInputController: ValidatedInputController<ClosedRange<Instant>, ValidatedHabitTrackTime>,
    creationController: SingleRequestController,
    habitController: LoadingController<Habit?>,
    commentInputController: ValidatedInputController<String, Nothing>
) {
    val logicModule = LocalLogicModule.current
    val uiModule = LocalUiModule.current
    val dateTimeConfigProvider = logicModule.dateTimeConfigProvider
    val dateTimeConfigState = dateTimeConfigProvider.configFlow().collectAsState(initial = null)
    val dateTimeConfig = dateTimeConfigState.value ?: return

    val dateTimeProvider = logicModule.dateTimeProvider
    val dateTimeFormatter = uiModule.dateTimeFormatter
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val eventCountState by eventCountInputController.collectState()
    val rangeState by timeInputController.collectState()
    var selectedTimeSelectionIndex by remember { mutableStateOf(0) }

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        val epicCalendarState = rememberSelectionEpicCalendarState(
            timeZone = dateTimeConfig.appTimeZone,
            initialRange = rangeState.input
        )

        SelectionEpicCalendarDialog(
            state = epicCalendarState,
            onSelected = {
                rangeSelectionShow = false
                selectedTimeSelectionIndex = 2
                timeInputController.changeInput(
                    it.withZeroSeconds(dateTimeConfig.appTimeZone)
                )
            },
            onCancel = {
                rangeSelectionShow = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.habitEventCreation_title),
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(4.dp))

        LoadingBox(habitController) {
            if (it != null) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(
                        R.string.habitEventCreation_habitName,
                        it.name
                    ),
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
                    dateTimeProvider.currentTime.value
                        .withZeroSeconds(dateTimeConfig.appTimeZone)
                        .asRangeOfOne()
                )
            }

            if (selectedTimeSelectionIndex == 1) {
                timeInputController.changeInput(
                    dateTimeProvider.currentTime.value.minus(1.days)
                        .withZeroSeconds(dateTimeConfig.appTimeZone)
                        .asRangeOfOne()
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
                    val start = dateTimeFormatter.formatDateTime(it.start)
                    "Дата и время: $start"
                } else {
                    val start = dateTimeFormatter.formatDateTime(it.start)
                    val end = dateTimeFormatter.formatDateTime(it.endInclusive)
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
            text = stringResource(R.string.habitEventCreation_comment_description)
        )


        Spacer(Modifier.height(12.dp))

        ValidatedTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            label = stringResource(R.string.habitEventCreation_comment),
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
            text = stringResource(R.string.habitEventCreation_finish_description)
        )

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            controller = creationController,
            text = stringResource(R.string.habitEventCreation_finish),
            type = Button.Type.Main,
            icon = {
                LocalResourceIcon(resourceId = R.drawable.ic_done)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}