package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackRange
import breakbadhabits.foundation.controller.DataFlowController
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.DataFlowBox
import breakbadhabits.foundation.uikit.IntervalSelectionEpicCalendarDialog
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
fun HabitTrackCreationScreen(
    eventCountInputController: ValidatedInputController<HabitTrack.EventCount, ValidatedHabitTrackEventCount>,
    rangeInputController: ValidatedInputController<HabitTrack.Range, ValidatedHabitTrackRange>,
    creationController: RequestController,
    habitController: DataFlowController<Habit?>,
    commentInputController: ValidatedInputController<HabitTrack.Comment?, Nothing>
) {
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val rangeState by rangeInputController.state.collectAsState()

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        IntervalSelectionEpicCalendarDialog(
            onSelected = {
                rangeSelectionShow = false
                val start = LocalDateTime(it.start.toKotlinLocalDate(), LocalTime(0, 0))
                val end = LocalDateTime(it.endInclusive.toKotlinLocalDate(), LocalTime(0, 0))
                rangeInputController.changeInput(HabitTrack.Range(start..end))
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
    ) {
        Title(
            modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
            text = stringResource(R.string.habitEventCreation_title)
        )

        DataFlowBox(habitController) {
            if (it != null) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
                    text = stringResource(
                        R.string.habitEventCreation_habitName,
                        it.name.value
                    )
                )
            }
        }

        ValidatedInputField(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            controller = eventCountInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it.value.toString() },
                    encodeInput = {
                        try {
                            HabitTrack.EventCount(it.toInt(), HabitTrack.EventCount.TimeUnit.DAYS)
                        } catch (e: Exception) {
                            HabitTrack.EventCount(0, HabitTrack.EventCount.TimeUnit.DAYS)
                        }
                    },
                    extractErrorMessage = {
                        val incorrect =
                            (it as? IncorrectHabitTrackEventCount) ?: return@TextFieldAdapter null
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

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = "Интервал:"
        )

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

        Button(
            modifier = Modifier.padding(16.dp),
            onClick = { rangeSelectionShow = true },
            text = rangeState.input.let {
                val start = formatter.format(it.value.start.date.toJavaLocalDate())
                val end = formatter.format(it.value.endInclusive.date.toJavaLocalDate())
                "Первое событие: $start, последнее событие: $end"
            }
        )

        Text(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.habitEventCreation_comment_description)
        )

        ValidatedInputField(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                .fillMaxWidth(),
            label = stringResource(R.string.habitEventCreation_comment),
            controller = commentInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it?.value ?: "" },
                    encodeInput = { HabitTrack.Comment(it) },
                    extractErrorMessage = { null }
                )
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 32.dp)
                .align(Alignment.End),
            text = stringResource(R.string.habitEventCreation_finish_description)
        )

        RequestButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            requestController = creationController,
            text = stringResource(R.string.habitEventCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}