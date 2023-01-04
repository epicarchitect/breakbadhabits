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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.LocalHabitIconResources
import breakbadhabits.android.app.LocalPresentationModule
import breakbadhabits.android.app.R
import breakbadhabits.android.app.rememberEpicViewModel
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.datetime.LocalDateInterval
import breakbadhabits.extension.datetime.toLocalDateTimeInterval
import breakbadhabits.logic.HabitCountability
import breakbadhabits.logic.IncorrectHabitNewName
import breakbadhabits.presentation.HabitCreationViewModel
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.Checkbox
import breakbadhabits.ui.kit.Dialog
import breakbadhabits.ui.kit.ErrorText
import breakbadhabits.ui.kit.IconData
import breakbadhabits.ui.kit.IconsSelection
import breakbadhabits.ui.kit.InteractionType
import breakbadhabits.ui.kit.IntervalSelectionEpicCalendar
import breakbadhabits.ui.kit.IntervalSelectionEpicCalendarDialog
import breakbadhabits.ui.kit.ProgressIndicator
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.TextField
import breakbadhabits.ui.kit.Title
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDate

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
    val focusManager = LocalFocusManager.current
    val habitIconResources = LocalHabitIconResources.current
    var intervalSelectionShow by remember { mutableStateOf(false) }

    if (intervalSelectionShow) {
        IntervalSelectionEpicCalendarDialog(
            onSelected = {
                intervalSelectionShow = false
                viewModel.updateFirstTrackInterval(
                    HabitTrack.Interval(it.toLocalDateTimeInterval())
                )
            },
            onCancel = {
                intervalSelectionShow = false
            }
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

        var dateYear by remember { mutableStateOf<Int?>(null) }
        var dateMonth by remember { mutableStateOf<Int?>(null) }
        var dateDay by remember { mutableStateOf<Int?>(null) }

        DateTextField(
            date = null,
            onDateChanged = {

            }
        )

        Button(onClick = { intervalSelectionShow = true }, text = "Select")

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

@Composable
fun DateTextField(
    date: LocalDate?,
    onDateChanged: (LocalDate?) -> Unit,
    separator: String = "/"
) {
    var text by rememberSaveable(date) {
        mutableStateOf(
            date?.let {
                "${it.dayOfMonth}${it.monthValue}${it.year}"
            } ?: ""
        )
    }
    TextField(
        value = text,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            if (it.length <= 8) {
                text = it.filter { it in "0123456789" }
            } else {
                // show something
            }

            if (it.length == 8) {
                onDateChanged(
                    LocalDate.of(
                        it.substring(0, 2).toInt(),
                        it.substring(2, 4).toInt(),
                        it.substring(4, 8).toInt()
                    )
                )
            }
        },
        visualTransformation = {
            TransformedText(
                text = AnnotatedString(
                    buildString {
                        it.text.forEachIndexed { index, char ->
                            append(char)
                            if (index == 1 || index == 3) {
                                append(separator)
                            }
                        }
                    }
                ),
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int) = when {
                        offset <= 1 -> offset
                        offset <= 3 -> offset + 1
                        else -> offset + 2
                    }

                    override fun transformedToOriginal(offset: Int) = when {
                        offset <= 2 -> offset
                        offset <= 5 -> offset - 1
                        else -> offset - 2
                    }
                }
            )
        }
    )
}