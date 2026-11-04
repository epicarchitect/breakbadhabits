package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitEventCountError
import epicarchitect.breakbadhabits.habits.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.HabitsRules
import epicarchitect.breakbadhabits.habits.checkHabitEventCount
import epicarchitect.breakbadhabits.habits.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.comment_description
import epicarchitect.breakbadhabits.resources.comment_title
import epicarchitect.breakbadhabits.resources.end_label
import epicarchitect.breakbadhabits.resources.event_count_description
import epicarchitect.breakbadhabits.resources.event_count_error_empty
import epicarchitect.breakbadhabits.resources.event_count_title
import epicarchitect.breakbadhabits.resources.event_record_delete_button
import epicarchitect.breakbadhabits.resources.event_record_editing_title_editing
import epicarchitect.breakbadhabits.resources.event_record_editing_title_new
import epicarchitect.breakbadhabits.resources.save_changes
import epicarchitect.breakbadhabits.resources.start_label
import epicarchitect.breakbadhabits.resources.time_range_description
import epicarchitect.breakbadhabits.resources.time_range_error_future
import epicarchitect.breakbadhabits.resources.time_range_title
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.uikit.DateTimeRangeInputCard
import epicarchitect.breakbadhabits.uikit.TextInputCard
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState


@Composable
fun HabitEventRecordEditingScreen(
    habitEventRecordId: Uuid?,
    habitId: Uuid
) {
    val state = rememberHabitEventRecordEditingState(
        eventRecordId = habitEventRecordId,
        habitId = habitId
    ) ?: return

    val backStack = LocalRootBackStack.current
    val habitRules = HabitsRules
    val habitEventRecordsDao = AppDatabase.habitEventRecordsDao
    val timeZone = TimeZone.currentSystemDefault()
    val coroutineScope = rememberCoroutineScope()

    var showDeletion by remember { mutableStateOf(false) }

    if (showDeletion) {
        HabitEventRecordDeletionDialog(
            recordId = state.initialRecord!!.id,
            onDismiss = { showDeletion = false },
            onDeleted = { backStack.removeLastOrNull() }
        )
    }
    val listState = rememberLazyListState()
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(bottom = bottomInset),
        state = listState
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(
                            if (state.initialRecord == null) {
                                Res.string.event_record_editing_title_new
                            } else {
                                Res.string.event_record_editing_title_editing
                            },
                            state.habit.name
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }

        item(key = "content") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))

                    TextInputCard(
                        title = stringResource(Res.string.event_count_title),
                        description = stringResource(Res.string.event_count_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = state.eventCount.let {
                            if (it == 0) ""
                            else it.toString()
                        },
                        onValueChange = {
                            val value = it.toIntOrNull() ?: 0
                            if (value <= habitRules.maxEventCount) {
                                state.eventCount = value
                                state.eventCountError = null
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        error = state.eventCountError?.let { error ->
                            when (error) {
                                HabitEventCountError.Empty -> stringResource(
                                    Res.string.event_count_error_empty
                                )
                            }
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    DateTimeRangeInputCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        title = stringResource(Res.string.time_range_title),
                        description = stringResource(Res.string.time_range_description),
                        error = state.timeRangeError?.let { error ->
                            when (error) {
                                HabitEventRecordTimeRangeError.BiggestThenCurrentTime -> stringResource(
                                    Res.string.time_range_error_future
                                )
                            }
                        },
                        value = state.timeRange,
                        onChanged = {
                            state.timeRange = it.ascended()
                            state.timeRangeError = null
                        },
                        startTimeLabel = stringResource(Res.string.start_label),
                        endTimeLabel = stringResource(Res.string.end_label),
                        timeZone = timeZone,
                        showAsRangeMode = state.dateTimeInputAsRange,
                        onRangeModeChanged = { state.dateTimeInputAsRange = it }
                    )

                    Spacer(Modifier.height(16.dp))

                    TextInputCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        title = stringResource(Res.string.comment_title),
                        description = stringResource(Res.string.comment_description),
                        value = state.comment,
                        onValueChange = {
                            state.comment = it
                        },
                        multiline = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.initialRecord != null) {
                        OutlinedButton(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = {
                                showDeletion = true
                            }
                        ) {
                            Text(
                                text = stringResource(Res.string.event_record_delete_button)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.End),
                        onClick = {
                            coroutineScope.launch {
                                state.eventCountError = checkHabitEventCount(state.eventCount)
                                if (state.eventCountError != null) return@launch

                                state.timeRangeError = checkHabitEventRecordTimeRange(
                                    timeRange = state.timeRange,
                                    currentTime = Clock.System.now()
                                )
                                if (state.timeRangeError != null) return@launch

                                if (state.initialRecord == null) {
                                    habitEventRecordsDao.insert(
                                        id = Uuid.generateV7(),
                                        habitId = state.habit.id,
                                        startTime = state.timeRange.start,
                                        endTime = state.timeRange.endInclusive,
                                        eventCount = state.eventCount,
                                        comment = state.comment
                                    )
                                } else {
                                    habitEventRecordsDao.update(
                                        id = state.initialRecord.id,
                                        startTime = state.timeRange.start,
                                        endTime = state.timeRange.endInclusive,
                                        eventCount = state.eventCount,
                                        comment = state.comment
                                    )
                                }

                                backStack.removeLastOrNull()
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.save_changes)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
    
                }
            }
        }
    }
}
