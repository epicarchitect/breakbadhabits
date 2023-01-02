package breakbadhabits.android.app.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.LocalHabitIconResources
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.android.app.rememberEpicViewModel
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.logic.HabitCountability
import breakbadhabits.logic.IncorrectHabitNewName
import breakbadhabits.presentation.HabitCreationViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Checkbox
import breakbadhabits.ui.kit.ErrorText
import breakbadhabits.ui.kit.IconData
import breakbadhabits.ui.kit.IconsSelection
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.EpicCalendar
import breakbadhabits.ui.kit.EpicCalendarInterval
import breakbadhabits.ui.kit.ProgressIndicator
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.TextField
import breakbadhabits.ui.kit.Title
import breakbadhabits.ui.kit.rememberEpicCalendarState
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HabitCreationScreen(onFinished: () -> Unit) {
    val appDependencies = LocalPresentationModule.current
    val habitCreationViewModel = rememberEpicViewModel {
        appDependencies.habitCreationModule.createHabitCreationViewModel()
    }
    val state = habitCreationViewModel.state.collectAsState()
    val viewModelState = state.value

    LaunchedEffect(viewModelState) {
        if (viewModelState is HabitCreationViewModel.State.Created) {
            onFinished()
        }
    }

    when (viewModelState) {
        is HabitCreationViewModel.State.Input -> InputScreen(
            habitCreationViewModel,
            viewModelState
        )

        is HabitCreationViewModel.State.Creating -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProgressIndicator()
            }
        }

        is HabitCreationViewModel.State.Created -> {
            // nothing
        }
    }
}

@Composable
private fun InputScreen(
    viewModel: HabitCreationViewModel,
    state: HabitCreationViewModel.State.Input
) {
    val intervalStartSelectionState = rememberMaterialDialogState()
    val intervalEndSelectionState = rememberMaterialDialogState()
    val focusManager = LocalFocusManager.current
    val habitIconResources = LocalHabitIconResources.current

    MaterialDialog(
        dialogState = intervalStartSelectionState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        },
        content = {
//            val initial = state.firstTrackInterval?.value?.start
//                ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            val initial = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            datepicker(
                title = stringResource(R.string.select_date),
                initialDate = java.time.LocalDate.of(
                    initial.year,
                    initial.month,
                    initial.dayOfMonth
                )
            ) { date ->
                val fakeInterval = HabitTrack.Interval(
                    LocalDateTimeInterval(
                        start = LocalDateTime(
                            date.year,
                            date.month,
                            date.dayOfMonth,
                            0,
                            0,
                            0
                        ),
                        end = LocalDateTime(
                            date.year,
                            date.month - 1,
                            date.dayOfMonth,
                            0,
                            0,
                            0
                        ),
                    )
                )

                viewModel.updateFirstTrackInterval(fakeInterval)
            }
        }
    )

    MaterialDialog(
        dialogState = intervalEndSelectionState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        },
        content = {
            val initial = state.firstTrackInterval?.value?.start
                ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            datepicker(
                title = stringResource(R.string.select_date),
                initialDate = java.time.LocalDate.of(
                    initial.year,
                    initial.month,
                    initial.dayOfMonth
                )
            ) { date ->
                val fakeInterval = HabitTrack.Interval(
                    LocalDateTimeInterval(
                        start = LocalDateTime(
                            date.year,
                            date.month,
                            date.dayOfMonth,
                            0,
                            0,
                            0
                        ),
                        end = LocalDateTime(
                            date.year,
                            date.month - 1,
                            date.dayOfMonth,
                            0,
                            0,
                            0
                        ),
                    )
                )

                viewModel.updateFirstTrackInterval(fakeInterval)
            }
        }
    )

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

        EpicCalendar(
            modifier = Modifier.fillMaxWidth(),
            state = rememberEpicCalendarState(yearMonth = YearMonth.now()),
            intervals = listOf(
                EpicCalendarInterval(
                    startDate = LocalDate.now().minusDays(1),
                    endDate = LocalDate.now().plusDays(5),
                    color = Color.Red.copy(
                        alpha = 0.5f
                    )
                ),
                EpicCalendarInterval(
                    startDate = LocalDate.now().plusDays(10),
                    endDate = LocalDate.now().plusDays(19),
                    color = Color.Green.copy(
                        alpha = 0.5f
                    )
                )
            ),
            horizontalInnerPadding = 16.dp,
            dayDefaultColor = Color.Black
        )

        TextField(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            value = state.name?.value ?: "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            onValueChange = {
                viewModel.updateName(Habit.Name(it))
            },
            label = stringResource(R.string.habitCreation_habitName),
            isError = state.validatedName is IncorrectHabitNewName
        )

        val validatedName = state.validatedName
        AnimatedVisibility(
            visible = validatedName is IncorrectHabitNewName
        ) {
            if (validatedName is IncorrectHabitNewName) {
                ErrorText(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                    text = when (val reason = validatedName.reason) {
                        is IncorrectHabitNewName.Reason.Empty -> {
                            stringResource(R.string.habitCreation_habitNameValidation_empty)
                        }

                        is IncorrectHabitNewName.Reason.TooLong -> {
                            stringResource(
                                R.string.habitCreation_habitNameValidation_tooLong,
                                reason.maxLength
                            )
                        }

                        is IncorrectHabitNewName.Reason.AlreadyUsed -> {
                            stringResource(R.string.habitCreation_habitNameValidation_used)
                        }
                    }
                )
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        IconsSelection(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            icons = state.icons.map {
                IconData(
                    it.iconId, habitIconResources[it.iconId]
                )
            },
            selectedIcon = habitIconResources.icons.first {
                it.iconId == state.selectedIcon.iconId
            }.let {
                IconData(it.iconId, it.resourceId)
            },
            onSelect = {
                viewModel.updateIconResource(Habit.IconResource(it.id))
            }
        )

        Row {
            Checkbox(
                checked = state.habitCountability is HabitCountability.Countable,
                onCheckedChange = {
                    viewModel.updateCountably(
                        if (it) {
                            HabitCountability.Countable(
                                HabitTrack.DailyCount(
                                    199.0 // TODO
                                )
                            )
                        } else {
                            HabitCountability.Uncountable()
                        }
                    )
                }
            )
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = "Habit is countable?"
            )
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_lastEvent_description)
        )

        Button(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            onClick = {
                intervalStartSelectionState.show()
            },
            text = "intervalStartSelectionState.show()"
        )

        Button(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            onClick = {
                intervalEndSelectionState.show()
            },
            text = "intervalEndSelectionState.show()"
        )

        Text(text = state.firstTrackInterval.toString())


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
            onClick = viewModel::startCreation,
            enabled = state.creationAllowed,
            text = stringResource(R.string.habitCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}