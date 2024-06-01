package epicarchitect.breakbadhabits.ui.habits.tracks.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.Habit
import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.operation.datetime.formatted
import epicarchitect.breakbadhabits.operation.habits.HabitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.operation.habits.dailyHabitEventCount
import epicarchitect.breakbadhabits.operation.habits.eventCountByDaily
import epicarchitect.breakbadhabits.operation.habits.habitTrackEventCountIncorrectReason
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.calendar.RangeSelectionCalendarDialog
import epicarchitect.breakbadhabits.uikit.calendar.RangeSelectionCalendarDialogResources
import epicarchitect.breakbadhabits.uikit.calendar.rememberSelectionCalendarState
import epicarchitect.breakbadhabits.uikit.regex.Regexps
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextField
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.addYears
import kotlinx.datetime.Month
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class HabitTrackEditingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackEditing(habitTrackId)
    }
}

@Composable
fun HabitTrackEditing(habitTrackId: Int) {
    FlowStateContainer(
        state = stateOfOneOrNull {
            AppData.database.habitTrackQueries.trackById(habitTrackId)
        }
    ) { habitTrack ->
        if (habitTrack != null) {
            FlowStateContainer(
                state = stateOfOneOrNull(habitTrack) {
                    // TODO maybe sql??
                    AppData.database.habitQueries.habitById(habitTrack.habitId)
                }
            ) { habit ->
                if (habit != null) {
                    Loaded(habit, habitTrack)
                }
            }
        }
    }
}

@Composable
private fun Loaded(
    habit: Habit,
    habitTrack: HabitTrack,
) {
    val habitTrackEditingStrings = AppData.resources.strings.habitTrackEditingStrings
    val habitTrackQueries = AppData.database.habitTrackQueries
    val navigator = LocalNavigator.currentOrThrow

    var rangeSelectionShow by rememberSaveable { mutableStateOf(false) }
    val timeZone by AppData.dateTime.currentTimeZoneState.collectAsState()

    var selectedDateTimeRange by remember(habitTrack) {
        mutableStateOf(habitTrack.startTime.toLocalDateTime(timeZone)..habitTrack.endTime.toLocalDateTime(timeZone))
    }

    var eventCount by rememberSaveable(habitTrack) {
        mutableIntStateOf(dailyHabitEventCount(habitTrack.eventCount, habitTrack.startTime, habitTrack.endTime, timeZone))
    }
    var eventCountIncorrectReason by remember {
        mutableStateOf<HabitTrackEventCountIncorrectReason?>(null)
    }
    var comment by rememberSaveable(habitTrack) {
        mutableStateOf(habitTrack.comment)
    }

    if (rangeSelectionShow) {
        val state = rememberSelectionCalendarState(
            initialSelectedDateTime = selectedDateTimeRange,
            monthRange = EpicMonth.now(timeZone).let {
                it.addYears(-10).copy(month = Month.JANUARY)..it.copy(month = Month.DECEMBER)
            }
        )

        RangeSelectionCalendarDialog(
            state = state,
            resources = RangeSelectionCalendarDialogResources(),
            onCancel = {
                rangeSelectionShow = false
            },
            onConfirm = {
                rangeSelectionShow = false
                selectedDateTimeRange = it
            }
        )
    }

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "habitTrackEditingStrings.deleteConfirmation()",
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = "habitTrackEditingStrings.cancel()",
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        text = "habitTrackEditingStrings.yes()",
                        type = Button.Type.Main,
                        onClick = {
                            habitTrackQueries.deleteById(habitTrack.id)
                            navigator.pop()
                        }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SimpleTopAppBar(
            title = habitTrackEditingStrings.titleText(),
            onBackClick = navigator::pop
        )

        Spacer(Modifier.height(4.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackEditingStrings.habitNameLabel(habit.name),
            type = Text.Type.Description,
            priority = Text.Priority.Low
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите сколько примерно было событий привычки каждый день"
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = eventCount.toString(),
            onValueChange = {
                eventCount = it.toIntOrNull() ?: 0
                eventCountIncorrectReason = null
            },
            label = "Число событий в день",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            regex = Regexps.integersOrEmpty(maxCharCount = 4),
            error = eventCountIncorrectReason?.let(habitTrackEditingStrings::trackEventCountError),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Укажите когда произошло событие:"
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = {
                rangeSelectionShow = true
            },
            text = selectedDateTimeRange.let {
                val start = it.start.formatted()
                val end = it.endInclusive.formatted()
                "Первое событие: $start, последнее событие: $end"
            }
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = habitTrackEditingStrings.commentDescription()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            value = comment,
            onValueChange = {
                comment = it
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "habitTrackEditingStrings.deleteDescription()"
        )


        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "habitTrackEditingStrings.deleteButton()",
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            text = habitTrackEditingStrings.finishDescription()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            text = habitTrackEditingStrings.finishButton(),
            type = Button.Type.Main,
            onClick = {
                eventCountIncorrectReason = habitTrackEventCountIncorrectReason(eventCount)
                if (eventCountIncorrectReason != null) return@Button

                val startTime = selectedDateTimeRange.start.toInstant(timeZone)
                val endTime = selectedDateTimeRange.endInclusive.toInstant(timeZone)

                habitTrackQueries.update(
                    id = habitTrack.id,
                    startTime = startTime,
                    endTime = endTime,
                    eventCount = eventCountByDaily(eventCount, startTime, endTime, timeZone),
                    comment = comment
                )
                navigator.pop()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}