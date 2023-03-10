package breakbadhabits.android.app.ui.habits

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.ui.LocalHabitIconResourceProvider
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitNewName
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackRange
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.Icon
import breakbadhabits.foundation.uikit.IntervalSelectionEpicCalendarDialog
import breakbadhabits.foundation.uikit.SingleSelectionGrid
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.regex.Regexps
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldAdapter
import breakbadhabits.foundation.uikit.text.Title
import breakbadhabits.foundation.uikit.text.ValidatedInputField
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@Composable
fun HabitCreationScreen(
    habitIconSelectionController: SingleSelectionController<Habit.Icon>,
    habitNameController: ValidatedInputController<Habit.Name, ValidatedHabitNewName>,
    firstTrackEventCountInputController: ValidatedInputController<HabitTrack.EventCount, ValidatedHabitTrackEventCount>,
    firstTrackRangeInputController: ValidatedInputController<HabitTrack.Range, ValidatedHabitTrackRange>,
    creationController: RequestController
) {
    val context = LocalContext.current
    val habitIconResources = LocalHabitIconResourceProvider.current
    var rangeSelectionShow by remember { mutableStateOf(false) }

    val firstTrackEventCountState by firstTrackEventCountInputController.state.collectAsState()
    val firstTrackRangeState by firstTrackRangeInputController.state.collectAsState()

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        IntervalSelectionEpicCalendarDialog(
            onSelected = {
                rangeSelectionShow = false
                val start = LocalDateTime(it.start.toKotlinLocalDate(), LocalTime(0, 0))
                val end = LocalDateTime(it.endInclusive.toKotlinLocalDate(), LocalTime(0, 0))
                firstTrackRangeInputController.changeInput(HabitTrack.Range(start..end))
            },
            onCancel = {
                rangeSelectionShow = false
            },
            maxYearMonth = YearMonth.now(),
            minYearMonth = YearMonth.now().minusYears(10),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Title(
            text = stringResource(R.string.habitCreation_title)
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
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    painter = painterResource(habitIconResources[icon].resourceId)
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Укажите даты первого и последнего события привычки."
        )

        Spacer(Modifier.height(16.dp))

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        Button(
            onClick = { rangeSelectionShow = true },
            text = firstTrackRangeState.input.let {
                val start = formatter.format(it.value.start.date.toJavaLocalDate())
                val end = formatter.format(it.value.endInclusive.date.toJavaLocalDate())
                "Первое событие: $start, последнее событие: $end"
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Укажите сколько примерно было событий привычки " + when (firstTrackEventCountState.input.timeUnit) {
                HabitTrack.EventCount.TimeUnit.HOURS -> "каждый час."
                HabitTrack.EventCount.TimeUnit.DAYS -> "каждый день."
            }
        )

        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            controller = firstTrackEventCountInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it.value.toString() },
                    encodeInput = {
                        firstTrackEventCountState.input.copy(
                            value = it.toIntOrNull() ?: 0
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
            label = "Число событий привычки",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty,
            trailingIcon = {
                val input = firstTrackEventCountState.input
                val timeUnit = input.timeUnit
                Button(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = {
                        firstTrackEventCountInputController.changeInput(
                            input.copy(
                                timeUnit = when (timeUnit) {
                                    HabitTrack.EventCount.TimeUnit.HOURS -> HabitTrack.EventCount.TimeUnit.DAYS
                                    HabitTrack.EventCount.TimeUnit.DAYS -> HabitTrack.EventCount.TimeUnit.HOURS
                                }
                            )
                        )
                    },
                    text = when (timeUnit) {
                        HabitTrack.EventCount.TimeUnit.HOURS -> "Каждый час"
                        HabitTrack.EventCount.TimeUnit.DAYS -> "Каждый день"
                    },
                    icon = {
                        Icon(painterResource(R.drawable.ic_change_circle))
                    }
                )
            }
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
            interactionType = InteractionType.MAIN
        )
    }
}