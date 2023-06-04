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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.app.feature.HabitEventTimeInputFeature
import breakbadhabits.android.app.formatter.DateTimeFormatter
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.ErrorText
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.TextField
import breakbadhabits.android.compose.ui.Title
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import epicarchitect.epicstore.compose.LocalEpicStore
import epicarchitect.epicstore.compose.rememberEpicStoreEntry
import epicarchitect.epicstore.getOrSet
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

@Composable
fun HabitEventEditingScreen(
    habitEventId: Int,
    onFinished: () -> Unit,
    onHabitEventDeleted: () -> Unit
) {
    val dateTimeFormatter: DateTimeFormatter = appDependencies.dateTimeFormatter
    val alertDialogManager: AlertDialogManager = appDependencies.alertDialogManager

    val timeInputFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventTimeInputFeature(habitEventId)
    }
    val commentFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventCommentInputFeature(habitEventId)
    }
    val habitIdFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventHabitIdFeature(habitEventId)
    }
    val deletionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventDeletionFeature()
    }
    val updatingFeature = rememberEpicStoreEntry {
        appDependencies.createHabitEventUpdatingFeature()
    }

    val comment by commentFeature.input.collectAsState()
    val eventTime by timeInputFeature.input.collectAsState()
    val eventTimeValidation by timeInputFeature.validation.collectAsState()
    val habitId by habitIdFeature.state.collectAsState()

    // TODO rewrite
    val epicStore = LocalEpicStore.current

    val habitNameFeature = remember(habitId) {
        habitId?.let {
            epicStore.getOrSet("habitNameFeature:$habitId") {
                appDependencies.createHabitNameFeature(it)
            }
        }
    }


    val habitName = habitNameFeature?.state?.collectAsState()

    val dateSelectionState = rememberMaterialDialogState()
    val timeSelectionState = rememberMaterialDialogState()

    val context = LocalContext.current

    MaterialDialog(
        dialogState = dateSelectionState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.cancel))
        }
    ) {
        val calendar = Calendar.getInstance().apply {
            eventTime?.let(::setTimeInMillis)
        }

        datepicker(
            initialDate = LocalDate.of(
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH] + 1,
                calendar[Calendar.DAY_OF_MONTH],
            ),
            title = stringResource(R.string.select_date)
        ) { date ->
            timeInputFeature.changeInput(
                Calendar.getInstance().apply {
                    eventTime?.let(::setTimeInMillis)
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
            eventTime?.let(::setTimeInMillis)
        }

        timepicker(
            initialTime = LocalTime.of(
                calendar[Calendar.HOUR],
                calendar[Calendar.MINUTE]
            ),
            is24HourClock = DateFormat.is24HourFormat(LocalContext.current),
            title = stringResource(R.string.select_time)
        ) { localTime ->
            timeInputFeature.changeInput(
                Calendar.getInstance().apply {
                    eventTime?.let(::setTimeInMillis)
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
            text = stringResource(R.string.habitEventEditing_habitName, habitName?.value ?: "")
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitEventEditing_event_description)
        )

        val calendar = Calendar.getInstance().apply {
            eventTime?.let(::setTimeInMillis)
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

        (eventTimeValidation as? HabitEventTimeInputFeature.ValidationState.Executed)?.let {
            when (it.result) {
                is HabitEventTimeInputFeature.ValidationResult.BiggestThenCurrentTime -> {
                    ErrorText(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        text = stringResource(R.string.habitEventEditing_eventTimeValidation_biggestThenCurrentTime)
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
            onValueChange = commentFeature::changeInput,
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
                        deletionFeature.startDeletion(habitEventId)
                        onHabitEventDeleted()
                    },
                )
            },
            text = stringResource(R.string.habitEventEditing_deletion_button),
            interactionType = InteractionType.DANGEROUS
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = {
                updatingFeature.startUpdating(
                    habitEventId,
                    eventTime!!,
                    comment
                )
                onFinished()
            },
            enabled = eventTimeValidation.let {
                it is HabitEventTimeInputFeature.ValidationState.Executed
                        && it.result is HabitEventTimeInputFeature.ValidationResult.Valid
            } && timeInputFeature.initialInput != eventTime || commentFeature.initialInput != comment,
            text = stringResource(R.string.habitEventEditing_finish),
            interactionType = InteractionType.MAIN
        )
    }
}