package breakbadhabits.app.android

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habit.creator.HabitCountability
import breakbadhabits.app.logic.habit.creator.IncorrectHabitNewName
import breakbadhabits.app.logic.habit.creator.ValidatedHabitNewName
import breakbadhabits.app.logic.habit.creator.ValidatedHabitTrackRange
import breakbadhabits.framework.controller.RequestController
import breakbadhabits.framework.controller.SingleSelectionController
import breakbadhabits.framework.controller.ValidatedInputController
import breakbadhabits.framework.uikit.Button
import breakbadhabits.framework.uikit.Checkbox
import breakbadhabits.framework.uikit.IconData
import breakbadhabits.framework.uikit.IconsSelection
import breakbadhabits.framework.uikit.InteractionType
import breakbadhabits.framework.uikit.IntervalSelectionEpicCalendarDialog
import breakbadhabits.framework.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.framework.uikit.text.Text
import breakbadhabits.framework.uikit.text.TextFieldAdapter
import breakbadhabits.framework.uikit.text.Title
import breakbadhabits.framework.uikit.text.ValidatedInputField
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private object Defaults {
    val countabilityRegex = "^\$|^[1-9][0-9]{0,9}$".toRegex()
}

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

    val habitIconSelectionState by habitIconSelectionController.state.collectAsState()
    val habitCountabilityState by habitCountabilityController.state.collectAsState()
    val firstTrackRangeState by firstTrackRangeInputController.state.collectAsState()
    val creationState by creationController.state.collectAsState()

    val habitNameAdapter = remember {
        TextFieldAdapter<Habit.Name, ValidatedHabitNewName>(
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
    }

    val habitCountabilityAdapter = remember {
        TextFieldAdapter<HabitCountability?, Unit>(
            decodeInput = {
                when (it) {
                    is HabitCountability.Countable -> {
                        it.averageDailyCount.value.toInt().toString()
                    }
                    else -> ""
                }
            },
            encodeInput = {
                try {
                    HabitCountability.Countable(
                        HabitTrack.DailyCount(
                            it.toDouble()
                        )
                    )
                } catch (e: Exception) {
                    HabitCountability.Countable(
                        HabitTrack.DailyCount(
                            0.0
                        )
                    )
                }
            },
            extractErrorMessage = { null }
        )
    }

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
            adapter = habitNameAdapter,
            label = stringResource(R.string.habitCreation_habitName)
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        IconsSelection(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            icons = habitIconSelectionState.items.map {
                IconData(
                    it.iconId,
                    habitIconResources[it.iconId]
                )
            },
            selectedIcon = habitIconResources.icons.first {
                it.iconId == habitIconSelectionState.selectedItem.iconId
            }.let {
                IconData(it.iconId, it.resourceId)
            },
            onSelect = {
                habitIconSelectionController.select(Habit.IconResource(it.id))
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val checked = habitCountabilityState.input is HabitCountability.Countable
                    habitCountabilityController.changeInput(
                        if (!checked) {
                            HabitCountability.Countable(HabitTrack.DailyCount(0.0))
                        } else {
                            HabitCountability.Uncountable()
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
                            HabitCountability.Countable(HabitTrack.DailyCount(0.0))
                        } else {
                            HabitCountability.Uncountable()
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
                adapter = habitCountabilityAdapter,
                label = "Число событий привычки в день",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                regex = Defaults.countabilityRegex
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

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = creationController::request,
            enabled = creationState.isRequestAllowed, // TODO resolve
            text = stringResource(R.string.habitCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}