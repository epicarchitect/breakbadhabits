package epicarchitect.breakbadhabits.screens.habits.tracks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.controller.InputController
import epicarchitect.breakbadhabits.foundation.controller.LoadingController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTimeRange
import epicarchitect.breakbadhabits.foundation.datetime.withZeroSeconds
import epicarchitect.breakbadhabits.foundation.math.ranges.isStartSameAsEnd
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import epicarchitect.breakbadhabits.foundation.uikit.LoadingBox
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RequestButton
import epicarchitect.breakbadhabits.foundation.uikit.calendar.SelectionEpicCalendarDialog
import epicarchitect.breakbadhabits.foundation.uikit.calendar.rememberSelectionEpicCalendarState
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.regex.Regexps
import epicarchitect.breakbadhabits.foundation.uikit.text.ErrorText
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextField
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldInputAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedInputField
import epicarchitect.breakbadhabits.logic.habits.model.Habit
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.IncorrectHabitTrackTime
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.ValidatedHabitTrackTime
import epicarchitect.breakbadhabits.screens.LocalAppModule
import kotlinx.datetime.TimeZone

//<string name="habitEventEditing_title">Editing an event</string>
//<string name="habitEventEditing_habitName">Habit: %s</string>
//<string name="habitEventEditing_event_description">Select the date and time when the event occurred.</string>
//<string name="habitEventEditing_comment_description">You can write a comment, but you don\'t have to.</string>
//<string name="habitEventEditing_comment">Comment</string>
//<string name="habitEventEditing_finish">Save changes</string>
//<string name="habitEventEditing_eventTimeValidation_biggestThenCurrentTime">The date and time of the event cannot be greater than the current time.</string>
//<string name="habitEventEditing_eventDate">Event date: %s</string>
//<string name="habitEventEditing_eventTime">Event time: %s</string>
//<string name="habitEventEditing_deletion_description">You can delete this event.</string>
//<string name="habitEventEditing_deletion_button">Delete this event</string>


interface HabitTrackUpdatingResources {
    val titleText: String
    fun habitNameLabel(name: String): String
}
@Composable
fun HabitTrackUpdating(
    eventCountInputController: ValidatedInputController<Int, ValidatedHabitTrackEventCount>,
    timeInputController: ValidatedInputController<ZonedDateTimeRange, ValidatedHabitTrackTime>,
    updatingController: SingleRequestController,
    deletionController: SingleRequestController,
    habitController: LoadingController<Habit?>,
    commentInputController: InputController<String>
) {
    val logicModule = LocalAppModule.current.logic
    val uiModule = LocalAppModule.current.ui
    val timeZone by logicModule.dateTime.dateTimeProvider.currentTimeZoneFlow()
        .collectAsState(TimeZone.currentSystemDefault())

    val dateTimeFormatter = uiModule.format.dateTimeFormatter
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val rangeState by timeInputController.state.collectAsState()

    ClearFocusWhenKeyboardHiddenEffect()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = "stringResource(R.string.habitEvents_deleteConfirmation)",
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = "stringResource(R.string.cancel)",
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RequestButton(
                        controller = deletionController,
                        text = "stringResource(R.string.yes)",
                        type = Button.Type.Main
                    )
                }
            }
        }
    }

    if (rangeSelectionShow) {
        val epicCalendarState = rememberSelectionEpicCalendarState(
            timeZone = timeZone,
            initialRange = rangeState.input.let {
                it.start.instant..it.endInclusive.instant
            }
        )

        SelectionEpicCalendarDialog(
            state = epicCalendarState,
            onSelected = {
                rangeSelectionShow = false
                timeInputController.changeInput(
                    it.withZeroSeconds(timeZone).let {
                        ZonedDateTimeRange.of(it.start, it.endInclusive, timeZone)
                    }
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
            .padding(16.dp)
    ) {
        Text(
            text = "stringResource(R.string.habitEventEditing_title)",
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(Modifier.height(8.dp))

        LoadingBox(habitController) {
            if (it != null) {
                Text(
//                    text = stringResource(
//                        R.string.habitEventEditing_habitName,
//                        it.name
//                    ),
                    text = it.name
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(text = "Укажите сколько примерно было событий привычки каждый день")

        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            controller = eventCountInputController,
            inputAdapter = remember {
                TextFieldInputAdapter(
                    decodeInput = { it.toString() },
                    encodeInput = { it.toIntOrNull() ?: 0 }
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
            text = "Укажите даты первого и последнего события привычки."
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { rangeSelectionShow = true },
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
                    ErrorText(text = "Нельзя выбрать время больше чем текущее")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitEventEditing_comment_description)"
        )


        Spacer(Modifier.height(16.dp))

        TextField(
            label = "stringResource(R.string.habitEventEditing_comment)",
            controller = commentInputController
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitEventEditing_deletion_description)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = "stringResource(R.string.habitEventEditing_deletion_button)",
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            controller = updatingController,
            text = "stringResource(R.string.habitEventEditing_finish)",
            type = Button.Type.Main,
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_done)
//            }
        )
    }
}