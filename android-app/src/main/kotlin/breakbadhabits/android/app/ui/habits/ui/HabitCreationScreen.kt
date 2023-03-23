package breakbadhabits.android.app.ui.habits.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.app.LocalDateTimeConfigProvider
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.android.app.ui.app.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.ValidatedHabitNewName
import breakbadhabits.app.logic.habits.tracks.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.tracks.IncorrectHabitTrackTime
import breakbadhabits.app.logic.habits.tracks.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.tracks.ValidatedHabitTrackTime
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.datetime.toInstantRange
import breakbadhabits.foundation.datetime.toJavaLocalDateTimeRange
import breakbadhabits.foundation.datetime.toKotlinRange
import breakbadhabits.foundation.uikit.LocalResourceIcon
import breakbadhabits.foundation.uikit.SingleSelectionGrid
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.calendar.IntervalSelectionEpicCalendarDialog
import breakbadhabits.foundation.uikit.calendar.rememberRangeSelectionEpicCalendarState
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.regex.Regexps
import breakbadhabits.foundation.uikit.text.ErrorText
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldAdapter
import breakbadhabits.foundation.uikit.text.ValidatedInputField
import kotlinx.datetime.toJavaZoneId
import java.time.YearMonth


@Composable
fun HabitCreationScreen(
    habitIconSelectionController: SingleSelectionController<Habit.Icon>,
    habitNameController: ValidatedInputController<Habit.Name, ValidatedHabitNewName>,
    firstTrackEventCountInputController: ValidatedInputController<HabitTrack.EventCount, ValidatedHabitTrackEventCount>,
    firstTrackTimeInputController: ValidatedInputController<HabitTrack.Time, ValidatedHabitTrackTime>,
    creationController: RequestController
) {
    val dateTimeConfigProvider = LocalDateTimeConfigProvider.current
    val dateTimeConfigState = dateTimeConfigProvider.configFlow().collectAsState(initial = null)
    val dateTimeConfig = dateTimeConfigState.value ?: return

    val context = LocalContext.current
    val dateTimeFormatter = LocalDateTimeFormatter.current
    val habitIconResources = LocalHabitIconResourceProvider.current
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val nowYearMonth = remember(dateTimeConfig) {
        YearMonth.now(dateTimeConfig.appTimeZone.toJavaZoneId())
    }

    val firstTrackEventCountState by firstTrackEventCountInputController.collectState()
    val firstTrackRangeState by firstTrackTimeInputController.collectState()

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        val epicCalendarState = rememberRangeSelectionEpicCalendarState(
            currentMonth = nowYearMonth,
            maxMonth = nowYearMonth,
            minMonth = nowYearMonth.minusYears(10),
            initialRange = firstTrackRangeState.input.toJavaLocalDateTimeRange(dateTimeConfig.appTimeZone)
        )

        IntervalSelectionEpicCalendarDialog(
            state = epicCalendarState,
            onSelected = {
                rangeSelectionShow = false
                firstTrackTimeInputController.changeInput(
                    HabitTrack.Time.of(
                        it.toKotlinRange().toInstantRange(dateTimeConfig.appTimeZone)
                    )
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
        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitCreation_title),
            type = Text.Type.Headline
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitCreation_habitName_description)
        )

        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            controller = habitNameController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = Habit.Name::value,
                    encodeInput = Habit::Name,
                    extractErrorMessage = {
                        val incorrect = (it as? IncorrectHabitNewName)
                            ?: return@TextFieldAdapter null
                        when (incorrect.reason) {
                            is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                                context.getString(R.string.habitCreation_habitNameValidation_used)
                            }

                            is IncorrectHabitNewName.Reason.Empty -> {
                                context.getString(R.string.habitCreation_habitNameValidation_empty)
                            }

                            is IncorrectHabitNewName.Reason.TooLong -> {
                                context.getString(
                                    R.string.habitCreation_habitNameValidation_tooLong,
                                    (incorrect.reason as IncorrectHabitNewName.Reason.TooLong).maxLength
                                )
                            }
                        }
                    }
                )
            },
            label = stringResource(R.string.habitCreation_habitName)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        Spacer(Modifier.height(16.dp))

        SingleSelectionGrid(
            controller = habitIconSelectionController,
            ceil = { icon ->
                LocalResourceIcon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    resourceId = habitIconResources[icon].resourceId
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Укажите даты первого и последнего события привычки."
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { rangeSelectionShow = true },
            text = firstTrackRangeState.input.let {
                val start = dateTimeFormatter.formatDateTime(it.start)
                val end = dateTimeFormatter.formatDateTime(it.endInclusive)
                "Первое событие: $start, последнее событие: $end"
            }
        )

        (firstTrackRangeState.validationResult as? IncorrectHabitTrackTime)?.let {
            Spacer(Modifier.height(8.dp))
            when (it.reason) {
                IncorrectHabitTrackTime.Reason.BiggestThenCurrentTime -> {
                    ErrorText(text = "Нельзя выбрать время больше чем текущее")
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Укажите сколько примерно было событий привычки каждый день"
        )

        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            controller = firstTrackEventCountInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it.dailyCount.toString() },
                    encodeInput = {
                        firstTrackEventCountState.input.copy(
                            dailyCount = it.toIntOrNull() ?: 0
                        )
                    },
                    extractErrorMessage = {
                        val incorrect = (it as? IncorrectHabitTrackEventCount)
                            ?: return@TextFieldAdapter null
                        when (incorrect.reason) {
                            is IncorrectHabitTrackEventCount.Reason.Empty -> "Поле не может быть пустым"
                        }
                    }
                )
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier.align(Alignment.End),
            text = stringResource(R.string.habitCreation_finish_description)
        )

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            requestController = creationController,
            text = stringResource(R.string.habitCreation_finish),
            type = Button.Type.Main
        )
    }
}