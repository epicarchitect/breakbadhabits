package breakbadhabits.app.ui.screen

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import breakbadhabits.app.ui.LocalAppDependencies
import breakbadhabits.app.ui.LocalHabitIconResources
import breakbadhabits.app.ui.R
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.feature.habits.presentation.HabitCreationViewModel
import breakbadhabits.feature.habits.validator.HabitNewNameValidator
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Checkbox
import breakbadhabits.ui.kit.ErrorText
import breakbadhabits.ui.kit.IconData
import breakbadhabits.ui.kit.IconsSelection
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.TextField
import breakbadhabits.ui.kit.Title
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import epicarchitect.epicstore.compose.rememberEpicStoreEntry
import kolmachikhin.alexander.validation.Incorrect
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun HabitCreationScreen(onFinished: () -> Unit) {
    val appDependencies = LocalAppDependencies.current
    val habitCreationViewModel = rememberEpicStoreEntry {
        appDependencies.habitsFeatureFactory.createHabitCreationViewModel()
    }
    val state = habitCreationViewModel.state.collectAsState()
    val viewModelState = state.value


    if (viewModelState is HabitCreationViewModel.State.Created) {
        LaunchedEffect(true) {
            onFinished()
        }
    }

    when (viewModelState) {
        is HabitCreationViewModel.State.Created -> Text("Created")
        is HabitCreationViewModel.State.Creating -> Text("Creating")
        is HabitCreationViewModel.State.Input -> InputScreen(
            habitCreationViewModel,
            viewModelState
        )
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
            isError = state.validatedName is Incorrect
        )

        val validatedName = state.validatedName
        if (validatedName is Incorrect) {
            ErrorText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                text = when (val reason = validatedName.reason) {
                    is HabitNewNameValidator.IncorrectReason.Empty -> {
                        stringResource(R.string.habitCreation_habitNameValidation_empty)
                    }

                    is HabitNewNameValidator.IncorrectReason.TooLong -> {
                        stringResource(
                            R.string.habitCreation_habitNameValidation_tooLong,
                            reason.maxLength
                        )
                    }

                    is HabitNewNameValidator.IncorrectReason.AlreadyUsed -> {
                        stringResource(R.string.habitCreation_habitNameValidation_used)
                    }
                }
            )
        }
        Row {
            Checkbox(
                checked = state.countability is HabitCreationViewModel.HabitCountability.Countable,
                onCheckedChange = {
                    viewModel.updateCountably(
                        if (it) {
                            HabitCreationViewModel.HabitCountability.Countable(
                                HabitTrack.DailyCount(
                                    199
                                )
                            )
                        } else {
                            HabitCreationViewModel.HabitCountability.Uncountable()
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
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        IconsSelection(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            icons = habitIconResources.icons.map {
                IconData(
                    it.iconId, it.resourceId
                )
            },
            selectedIcon = habitIconResources.icons.first {
                it.iconId == (state.iconResource?.iconId ?: 0)
            }.let {
                IconData(it.iconId, it.resourceId)
            },
            onSelect = {
                viewModel.updateIconResource(Habit.IconResource(it.id))
            }
        )

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