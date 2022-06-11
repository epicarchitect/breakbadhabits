package breakbadhabits.android.app.compose.screen

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.app.viewmodel.HabitEventEditingViewModel
import breakbadhabits.android.compose.molecule.ActionType
import breakbadhabits.android.compose.molecule.Button
import breakbadhabits.android.compose.molecule.ErrorText
import breakbadhabits.android.compose.molecule.Text
import breakbadhabits.android.compose.molecule.TextField
import breakbadhabits.android.compose.molecule.Title
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

@Composable
fun HabitEventEditingScreen(
    habitEventEditingViewModel: HabitEventEditingViewModel,
    dateTimeFormatter: DateTimeFormatter,
    alertDialogManager: AlertDialogManager,
    onFinished: () -> Unit,
    onHabitEventDeleted: () -> Unit
) {
    val comment by habitEventEditingViewModel.commentStateFlow.collectAsState()
    val habitEventUpdatingAllowed by habitEventEditingViewModel.habitEventUpdatingAllowedStateFlow.collectAsState()
    val habitEventUpdating by habitEventEditingViewModel.habitEventUpdatingStateFlow.collectAsState()
    val eventTime by habitEventEditingViewModel.eventTimeStateFlow.collectAsState()
    val eventTimeValidation by habitEventEditingViewModel.eventTimeValidationStateFlow.collectAsState()
    val habit by habitEventEditingViewModel.habitFlow.collectAsState()
    val dateSelectionState = rememberMaterialDialogState()
    val timeSelectionState = rememberMaterialDialogState()
    val context = LocalContext.current

    if (habitEventUpdating is HabitEventEditingViewModel.HabitEventUpdatingState.Executed) {
        LaunchedEffect(true) {
            onFinished()
        }
    }

    MaterialDialog(
        dialogState = dateSelectionState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        val calendar = Calendar.getInstance().apply {
            eventTime.let(::setTimeInMillis)
        }

        datepicker(
            initialDate = LocalDate.of(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DAY_OF_MONTH],
            ),
            title = stringResource(R.string.select_date)
        ) { date ->
            habitEventEditingViewModel.updateEventTime(
                Calendar.getInstance().apply {
                    eventTime.let(::setTimeInMillis)
                    set(Calendar.YEAR, date.year)
                    set(Calendar.MONTH, date.monthValue - 1)
                    set(Calendar.DAY_OF_MONTH, date.dayOfMonth)
                }.timeInMillis
            )
        }
    }

    MaterialDialog(
        dialogState = timeSelectionState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        val calendar = Calendar.getInstance().apply {
            eventTime.let(::setTimeInMillis)
        }

        timepicker(
            initialTime = LocalTime.of(
                calendar[Calendar.HOUR],
                calendar[Calendar.MINUTE]
            ),
            is24HourClock = DateFormat.is24HourFormat(LocalContext.current),
            title = stringResource(R.string.select_time)
        ) { localTime ->
            habitEventEditingViewModel.updateEventTime(
                Calendar.getInstance().apply {
                    eventTime.let(::setTimeInMillis)
                    set(Calendar.HOUR_OF_DAY, localTime.hour)
                    set(Calendar.MINUTE, localTime.minute)
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
            text = stringResource(R.string.habitEventEditing_title)
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
            text = stringResource(R.string.habitEventEditing_habitName, habit?.name ?: "")
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitEventEditing_event_description)
        )

        val calendar = Calendar.getInstance().apply {
            eventTime.let(::setTimeInMillis)
        }

        Button(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            onClick = {
                dateSelectionState.show()
            },
            text = stringResource(
                R.string.habitEventEditing_eventDate,
                dateTimeFormatter.formatDate(calendar)
            )
        )

        Button(
            modifier = Modifier.padding(start = 16.dp, top = 2.dp, end = 16.dp),
            onClick = {
                timeSelectionState.show()
            },
            text = stringResource(
                R.string.habitEventEditing_eventTime,
                dateTimeFormatter.formatTime(calendar)
            )
        )

        (eventTimeValidation as? HabitEventEditingViewModel.EventTimeValidationState.Executed)?.let {
            when (it.result) {
                is HabitEventEditingViewModel.EventTimeValidationResult.BiggestThenCurrentTime -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitEventEditing_eventTimeValidation_biggestThenCurrentTime),
                    )
                }
                else -> {
                    /* no-op */
                }
            }
        }

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitEventEditing_comment_description)
        )

        TextField(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            value = comment ?: "",
            onValueChange = {
                habitEventEditingViewModel.updateComment(it)
            },
            label = stringResource(R.string.habitEventEditing_comment)
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitEventEditing_deletion_description)
        )

        Button(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            onClick = {
                alertDialogManager.showAlert(
                    context,
                    title = null,
                    message = context.getString(R.string.habitEvents_deleteConfirmation),
                    positiveButtonTitle = context.getString(R.string.yes),
                    negativeButtonTitle = context.getString(R.string.cancel),
                    onPositive = {
                        habitEventEditingViewModel.deleteEvent()
                        onHabitEventDeleted()
                    },
                )
            },
            text = stringResource(R.string.habitEventEditing_deletion_button),
            actionType = ActionType.DANGEROUS
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = {
                habitEventEditingViewModel.startHabitEventUpdating()
            },
            enabled = habitEventUpdatingAllowed,
            text = stringResource(R.string.habitEventEditing_finish),
            actionType = ActionType.MAIN
        )
    }
}