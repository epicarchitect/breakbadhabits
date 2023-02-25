package breakbadhabits.app.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.creator.HabitCountability
import breakbadhabits.app.logic.habits.validator.IncorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.ValidatedHabitNewName
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackRange
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.SingleSelectionController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.Checkbox
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
fun HabitCreationScreen(onFinished: () -> Unit) {
    val presentationModule = LocalPresentationModule.current
    val viewModel = viewModel {
        presentationModule.createHabitCreationViewModel()
    }

    val creationState by viewModel.creationController.state.collectAsState()

    LaunchedEffect(creationState) {
        if (creationState.requestState is RequestController.RequestState.Executed) {
            onFinished()
        }
    }

    Content(
        habitIconSelectionController = viewModel.habitIconSelectionController,
        habitNameController = viewModel.habitNameController,
        habitCountabilityController = viewModel.habitCountabilityController,
        firstTrackRangeInputController = viewModel.firstTrackRangeInputController,
        creationController = viewModel.creationController
    )
}

@Composable
private fun Content(
    habitIconSelectionController: SingleSelectionController<Habit.IconResource>,
    habitNameController: ValidatedInputController<Habit.Name, ValidatedHabitNewName>,
    habitCountabilityController: ValidatedInputController<HabitCountability?, Unit>,
    firstTrackRangeInputController: ValidatedInputController<HabitTrack.Range?, ValidatedHabitTrackRange>,
    creationController: RequestController
) {
    val context = LocalContext.current
    val habitIconResources = LocalHabitIconResources.current
    var intervalSelectionShow by remember { mutableStateOf(false) }

    val habitCountabilityState by habitCountabilityController.state.collectAsState()
    val firstTrackRangeState by firstTrackRangeInputController.state.collectAsState()

    ClearFocusWhenKeyboardHiddenEffect()

    if (intervalSelectionShow) {
        IntervalSelectionEpicCalendarDialog(
            onSelected = {
                intervalSelectionShow = false
                val start = LocalDateTime(it.start.toKotlinLocalDate(), LocalTime(0, 0))
                val end = LocalDateTime(it.endInclusive.toKotlinLocalDate(), LocalTime(0, 0))
                firstTrackRangeInputController.changeInput(HabitTrack.Range(start..end))
            },
            onCancel = {
                intervalSelectionShow = false
            },
            maxYearMonth = YearMonth.now(),
            minYearMonth = YearMonth.now().minusYears(10),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Title(
            modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
            text = stringResource(R.string.habitCreation_title)
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_habitName_description)
        )

        ValidatedInputField(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            controller = habitNameController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = Habit.Name::value,
                    encodeInput = Habit::Name,
                    extractErrorMessage = {
                        val incorrect = (it as? IncorrectHabitNewName) ?: return@TextFieldAdapter null
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

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        SingleSelectionGrid(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            controller = habitIconSelectionController,
            ceil = {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    painter = painterResource(habitIconResources[it.iconId])
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val checked = habitCountabilityState.input is HabitCountability.Countable
                    habitCountabilityController.changeInput(
                        if (!checked) {
                            HabitCountability.Countable(0.0)
                        } else {
                            HabitCountability.Uncountable
                        }
                    )
                }
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = habitCountabilityState.input is HabitCountability.Countable,
                onCheckedChange = {
                    habitCountabilityController.changeInput(
                        if (it) {
                            HabitCountability.Countable(0.0)
                        } else {
                            HabitCountability.Uncountable
                        }
                    )
                }
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = "Habit is countable?"
            )
        }

        if (habitCountabilityState.input is HabitCountability.Countable) {
            ValidatedInputField(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                controller = habitCountabilityController,
                adapter = remember {
                    TextFieldAdapter(
                        decodeInput = {
                            when (it) {
                                is HabitCountability.Countable -> {
                                    it.averageDailyValue.toInt().toString()
                                }
                                else -> ""
                            }
                        },
                        encodeInput = {
                            try {
                                HabitCountability.Countable(it.toDouble())
                            } catch (e: Exception) {
                                HabitCountability.Countable(0.0)
                            }
                        },
                        extractErrorMessage = { null }
                    )
                },
                label = "Число событий привычки в день",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                regex = Regexps.integersOrEmpty
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = "Укажите первое и последнее событие привычки:"
        )

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { intervalSelectionShow = true },
            text = firstTrackRangeState.input?.let {
                val start = formatter.format(it.value.start.date.toJavaLocalDate())
                val end = formatter.format(it.value.endInclusive.date.toJavaLocalDate())
                "Первое событие: $start, последнее событие: $end"
            } ?: "Указать первое и последнне событие"
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            text = stringResource(R.string.habitCreation_finish_description)
        )

        RequestButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            requestController = creationController,
            text = stringResource(R.string.habitCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}