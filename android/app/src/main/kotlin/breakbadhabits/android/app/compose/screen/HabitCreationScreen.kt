package breakbadhabits.android.app.compose.screen

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.createHabitCreationFeature
import breakbadhabits.android.app.createHabitIconSelectionFeature
import breakbadhabits.android.app.createHabitNameInputFeature
import breakbadhabits.android.app.createLastTimeInputFeature
import breakbadhabits.android.app.feature.HabitCreationFeature
import breakbadhabits.android.app.feature.HabitNameInputFeature
import breakbadhabits.android.app.feature.LastTimeInputFeature
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.resources.HabitIconResources
import breakbadhabits.android.app.utils.get
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.ErrorText
import breakbadhabits.android.compose.ui.IconData
import breakbadhabits.android.compose.ui.IconsSelection
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.TextField
import breakbadhabits.android.compose.ui.Title
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import epicarchitect.epicstore.compose.rememberEpicStoreEntry
import java.time.LocalDate
import java.time.LocalTime
import java.util.*


@Composable
fun HabitCreationScreen(onFinished: () -> Unit) {
    val dateTimeFormatter: DateTimeFormatter = get()
    val habitIconResources: HabitIconResources = get()
    val habitNameInputFeature = rememberEpicStoreEntry { createHabitNameInputFeature() }
    val lastEventTimeInputFeature = rememberEpicStoreEntry { createLastTimeInputFeature() }
    val habitIconSelectionFeature = rememberEpicStoreEntry { createHabitIconSelectionFeature() }
    val habitCreationFeature = rememberEpicStoreEntry { createHabitCreationFeature() }

    val habitName by habitNameInputFeature.input.collectAsState()
    val habitNameValidation by habitNameInputFeature.validation.collectAsState()
    val lastEventTime by lastEventTimeInputFeature.input.collectAsState()
    val lastHabitEventTimeValidation by lastEventTimeInputFeature.validation.collectAsState()
    val selectedIcon by habitIconSelectionFeature.selection.collectAsState()

    val habitNameValidationResult =
        (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)?.result

    val lastEventTimeValidationResult =
        (lastHabitEventTimeValidation as? LastTimeInputFeature.ValidationState.Executed)?.result

    val creationAllowed = habitNameValidationResult is HabitNameInputFeature.ValidationResult.Valid
            && lastEventTimeValidationResult is LastTimeInputFeature.ValidationResult.Valid

    val habitCreation by habitCreationFeature.state.collectAsState()

    val dateSelectionState = rememberMaterialDialogState()
    val timeSelectionState = rememberMaterialDialogState()
    val focusManager = LocalFocusManager.current

    if (habitCreation is HabitCreationFeature.State.Executed) {
        LaunchedEffect(true) {
            onFinished()
        }
    }

    MaterialDialog(dialogState = dateSelectionState, buttons = {
        positiveButton(stringResource(R.string.ok))
        negativeButton(stringResource(R.string.cancel))
    }) {
        val calendar = Calendar.getInstance().apply {
            lastEventTime?.let(::setTimeInMillis)
        }

        datepicker(
            initialDate = LocalDate.of(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DAY_OF_MONTH],
            ), title = stringResource(R.string.select_date)
        ) { date ->
            lastEventTimeInputFeature.changeInput(
                Calendar.getInstance().apply {
                    lastEventTime?.let(::setTimeInMillis) ?: let {
                        timeSelectionState.show()
                    }
                    set(Calendar.YEAR, date.year)
                    set(Calendar.MONTH, date.monthValue - 1)
                    set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                }.timeInMillis
            )
        }
    }

    MaterialDialog(dialogState = timeSelectionState, buttons = {
        positiveButton(stringResource(R.string.ok))
        negativeButton(stringResource(R.string.cancel))
    }) {
        val calendar = Calendar.getInstance().apply {
            lastEventTime?.let(::setTimeInMillis)
        }

        timepicker(
            initialTime = LocalTime.of(
                calendar[Calendar.HOUR], calendar[Calendar.MINUTE]
            ),
            is24HourClock = DateFormat.is24HourFormat(LocalContext.current),
            title = stringResource(R.string.select_time)
        ) { time ->
            lastEventTimeInputFeature.changeInput(
                Calendar.getInstance().apply {
                    lastEventTime?.let(::setTimeInMillis)
                    set(Calendar.HOUR_OF_DAY, time.hour)
                    set(Calendar.MINUTE, time.minute)
                }.timeInMillis
            )
        }
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
            value = habitName ?: "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            onValueChange = habitNameInputFeature::changeInput,
            label = stringResource(R.string.habitCreation_habitName),
            isError = habitNameValidation is HabitNameInputFeature.ValidationState.Executed && (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)?.result !is HabitNameInputFeature.ValidationResult.Valid
        )

        (habitNameValidation as? HabitNameInputFeature.ValidationState.Executed)?.let {
            when (it.result) {
                is HabitNameInputFeature.ValidationResult.Empty -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitCreation_habitNameValidation_empty),
                    )
                }
                is HabitNameInputFeature.ValidationResult.TooLong -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(
                            R.string.habitCreation_habitNameValidation_tooLong,
                            it.result.maxHabitNameLength
                        ),
                    )
                }
                is HabitNameInputFeature.ValidationResult.Used -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitCreation_habitNameValidation_used),
                    )
                }
                else -> {
                    /* no-op */
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_habitIcon_description)
        )

        IconsSelection(modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp)
            .fillMaxWidth(),
            icons = habitIconResources.icons.map {
                IconData(
                    it.iconId, it.resourceId
                )
            },
            selectedIcon = habitIconResources.icons.first { it.iconId == selectedIcon }.let {
                IconData(
                    it.iconId, it.resourceId
                )
            },
            onSelect = {
                habitIconSelectionFeature.changeSelection(it.id)
            })

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitCreation_lastEvent_description)
        )

        if (lastEventTime == null) {
            Button(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp), onClick = {
                    dateSelectionState.show()
                }, text = stringResource(R.string.habitCreation_selectLastEventTime)
            )
        } else {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = lastEventTime!!
            }

            Button(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp), onClick = {
                    dateSelectionState.show()
                }, text = stringResource(
                    R.string.habitCreation_lastEventTime_date,
                    dateTimeFormatter.formatDate(calendar)
                )
            )

            Button(
                modifier = Modifier.padding(start = 16.dp, top = 2.dp, end = 16.dp), onClick = {
                    timeSelectionState.show()
                }, text = stringResource(
                    R.string.habitCreation_lastEventTime_time,
                    dateTimeFormatter.formatTime(calendar)
                )
            )
        }

        (lastHabitEventTimeValidation as? LastTimeInputFeature.ValidationState.Executed)?.let {
            when (it.result) {
                is LastTimeInputFeature.ValidationResult.BiggestThenCurrentTime -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitCreation_lastEventTimeValidation_biggestThenCurrentTime),
                    )
                }
                else -> {
                    /* no-op */
                }
            }
        }

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
            onClick = {
                habitCreationFeature.startCreation(
                    habitName!!,
                    selectedIcon,
                    lastEventTime!!
                )
            },
            enabled = creationAllowed,
            text = stringResource(R.string.habitCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}