package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import breakbadhabits.android.app.ui.app.LocalDateTimeFormatter
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.IncorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.ValidatedHabitTrackRange
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.IntervalSelectionEpicCalendarDialog
import breakbadhabits.foundation.uikit.LoadingBox
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.regex.Regexps
import breakbadhabits.foundation.uikit.rememberRangeSelectionEpicCalendarState
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldAdapter
import breakbadhabits.foundation.uikit.text.Title
import breakbadhabits.foundation.uikit.text.ValidatedInputField
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth

@Composable
fun HabitTrackUpdatingScreen(
    eventCountInputController: ValidatedInputController<HabitTrack.EventCount, ValidatedHabitTrackEventCount>,
    rangeInputController: ValidatedInputController<HabitTrack.Range, ValidatedHabitTrackRange>,
    updatingController: RequestController,
    deletionController: RequestController,
    habitController: LoadingController<Habit?>,
    commentInputController: ValidatedInputController<HabitTrack.Comment?, Nothing>
) {
    val dateTimeFormatter = LocalDateTimeFormatter.current
    var rangeSelectionShow by remember { mutableStateOf(false) }
    val eventCountState by eventCountInputController.collectState()
    val rangeState by rangeInputController.collectState()

    ClearFocusWhenKeyboardHiddenEffect()

    if (rangeSelectionShow) {
        val epicCalendarState = rememberRangeSelectionEpicCalendarState(
            currentMonth = YearMonth.now(),
            maxMonth = YearMonth.now(),
            minMonth = YearMonth.now().minusYears(10),
            initialRange = rangeState.input.value.start
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .toJavaLocalDateTime()
                .toLocalDate()..rangeState.input.value.endInclusive
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .toJavaLocalDateTime()
                .toLocalDate()
        )

        IntervalSelectionEpicCalendarDialog(
            state = epicCalendarState,
            onSelected = {
                rangeSelectionShow = false
                val start = LocalDateTime(it.start.toKotlinLocalDate(), LocalTime(0, 0, 0, 0))
                val end = LocalDateTime(it.endInclusive.toKotlinLocalDate(), LocalTime(0, 0, 0, 0))
                rangeInputController.changeInput(
                    HabitTrack.Range(
                        start.toInstant(
                            TimeZone.currentSystemDefault()
                        )..end.toInstant(
                            TimeZone.currentSystemDefault()
                        )
                    )
                )
            },
            onCancel = {
                rangeSelectionShow = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(24.dp))

        Title(stringResource(R.string.habitEventEditing_title))

        Spacer(Modifier.height(8.dp))

        LoadingBox(habitController) {
            if (it != null) {
                Text(
                    text = stringResource(
                        R.string.habitEventEditing_habitName,
                        it.name.value
                    )
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(text = "Укажите сколько примерно было событий привычки каждый день")

        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            controller = eventCountInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it.dailyCount.toString() },
                    encodeInput = {
                        eventCountState.input.copy(
                            dailyCount = it.toIntOrNull() ?: 0
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
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Укажите даты первого и последнего события привычки."
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { rangeSelectionShow = true },
            text = rangeState.input.let {
                val start = dateTimeFormatter.formatInstantAsDate(it.value.start)
                val end = dateTimeFormatter.formatInstantAsDate(it.value.endInclusive)
                "Первое событие: $start, последнее событие: $end"
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEventEditing_comment_description)
        )


        Spacer(Modifier.height(16.dp))

        ValidatedInputField(
            label = stringResource(R.string.habitEventEditing_comment),
            controller = commentInputController,
            adapter = remember {
                TextFieldAdapter(
                    decodeInput = { it?.value ?: "" },
                    encodeInput = { if (it.isEmpty()) null else HabitTrack.Comment(it) },
                    extractErrorMessage = { null }
                )
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.habitEventEditing_deletion_description)
        )

        Spacer(modifier = Modifier.height(16.dp))

        RequestButton(
            requestController = deletionController,
            text = stringResource(R.string.habitEventEditing_deletion_button),
            interactionType = InteractionType.DANGEROUS
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            requestController = updatingController,
            text = stringResource(R.string.habitEventEditing_finish),
            interactionType = InteractionType.MAIN
        )
    }
}