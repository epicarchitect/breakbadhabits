package epicarchitect.breakbadhabits.screens.habits.eventRecords.editing

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitEventCommentError
import epicarchitect.breakbadhabits.habits.HabitEventCountError
import epicarchitect.breakbadhabits.habits.HabitEventRecordTimeRangeError
import epicarchitect.breakbadhabits.habits.HabitsRules
import epicarchitect.breakbadhabits.habits.checkHabitEventComment
import epicarchitect.breakbadhabits.habits.limitHabitEventComment
import epicarchitect.breakbadhabits.habits.checkHabitEventCount
import epicarchitect.breakbadhabits.habits.checkHabitEventRecordTimeRange
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.comment_description
import epicarchitect.breakbadhabits.resources.comment_error_too_long
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
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AsyncPrimaryButton
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.AsyncAction
import epicarchitect.breakbadhabits.uikit.DateTimeRangeInputCard
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.SecondaryButton
import epicarchitect.breakbadhabits.uikit.TextInputCard
import epicarchitect.breakbadhabits.uikit.rememberAsyncAction
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid


@Composable
fun HabitEventRecordEditingScreen(
    habitEventRecordId: Uuid?,
    habitId: Uuid
) {
    val state = rememberHabitEventRecordEditingState(
        eventRecordId = habitEventRecordId,
        habitId = habitId
    ) ?: return

    val navBackStack = LocalNavBackStack.current
    val habitRules = HabitsRules

    var showDeletion by remember { mutableStateOf(false) }
    val saveAction = rememberAsyncAction {
        state.eventCountError = checkHabitEventCount(state.eventCount)
        if (state.eventCountError != null) return@rememberAsyncAction

        if (state.commentError == null) {
            state.commentError = checkHabitEventComment(
                comment = state.comment,
                maxLength = habitRules.maxEventCommentLength
            )
        }
        if (state.commentError != null) return@rememberAsyncAction

        state.timeRangeError = checkHabitEventRecordTimeRange(
            timeRange = state.timeRange,
            currentTime = Clock.System.now()
        )
        if (state.timeRangeError != null) return@rememberAsyncAction

        if (state.initialRecord == null) {
            AppDatabase.habitEventRecordsDao.insert(
                id = Uuid.generateV7(),
                habitId = state.habit.id,
                startTime = state.timeRange.start,
                endTime = state.timeRange.endInclusive,
                eventCount = state.eventCount,
                comment = state.comment
            )
        } else {
            AppDatabase.habitEventRecordsDao.update(
                id = state.initialRecord.id,
                startTime = state.timeRange.start,
                endTime = state.timeRange.endInclusive,
                eventCount = state.eventCount,
                comment = state.comment
            )
        }

        navBackStack.removeLastOrNull()
    }

    if (showDeletion) {
        HabitEventRecordDeletionDialog(
            recordId = state.initialRecord!!.id,
            onDismiss = { showDeletion = false },
            onDeleted = { navBackStack.removeLastOrNull() }
        )
    }

    HabitEventRecordEditingScreenContent(
        state = state,
        timeZone = AppTimeZone,
        saveAction = saveAction,
        onBack = { navBackStack.removeLastOrNull() },
        onDelete = { showDeletion = true }
    )
}

@Composable
internal fun HabitEventRecordEditingScreenContent(
    state: HabitEventRecordEditingScreenState,
    timeZone: TimeZone,
    saveAction: AsyncAction,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
    val habitRules = HabitsRules
    val listState = rememberLazyListState()
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding(),
        contentPadding = PaddingValues(
            bottom = navigationBarBottom
        ),
        state = listState
    ) {
        item(key = "top_bar") {
            AppTopBar(
                title = stringResource(
                    if (state.initialRecord == null) {
                        Res.string.event_record_editing_title_new
                    } else {
                        Res.string.event_record_editing_title_editing
                    },
                    state.habit.name
                ),
                navigationIcon = {
                    AppBackButton(onClick = { onBack() })
                }
            )
        }

        item(key = "content") {
            AdaptiveContent {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(12.dp))

                    TextInputCard(
                        title = stringResource(Res.string.event_count_title),
                        description = stringResource(Res.string.event_count_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = if (state.eventCount == 0) {
                            ""
                        } else {
                            state.eventCount.toString()
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

                    Spacer(Modifier.height(12.dp))

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
                        timeZone = AppTimeZone,
                        showAsRangeMode = state.dateTimeInputAsRange,
                        onRangeModeChanged = { state.dateTimeInputAsRange = it }
                    )

                    Spacer(Modifier.height(12.dp))

                    TextInputCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        title = stringResource(Res.string.comment_title),
                        description = stringResource(Res.string.comment_description),
                        value = state.comment,
                        onValueChange = {
                            val limitedComment = limitHabitEventComment(
                                comment = it,
                                maxLength = habitRules.maxEventCommentLength
                            )
                            state.comment = limitedComment
                            state.commentError = checkHabitEventComment(
                                comment = limitedComment,
                                maxLength = habitRules.maxEventCommentLength
                            )
                        },
                        multiline = true,
                        error = state.commentError?.let { error ->
                            when (error) {
                                is HabitEventCommentError.TooLong -> stringResource(
                                    Res.string.comment_error_too_long,
                                    error.maxLength
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.initialRecord != null) {
                        SecondaryButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(Res.string.event_record_delete_button),
                            onClick = {
                                onDelete()
                            }
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    AsyncPrimaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        action = saveAction,
                        text = stringResource(Res.string.save_changes)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 900, name = "HabitEventRecordEditingScreen")
@Composable
private fun HabitEventRecordEditingScreenPreview() {
    PreviewTheme {
        val now = Clock.System.now()
        val state = remember {
            HabitEventRecordEditingScreenState(
                initialRecord = null,
                habit = Habit(
                    id = Uuid.random(),
                    name = "Smoking",
                    iconId = 0
                ),
                defaultTimeRange = now..now
            )
        }
        HabitEventRecordEditingScreenContent(
            state = state,
            timeZone = TimeZone.UTC,
            saveAction = PreviewIdleHabitEventRecordSaveAction,
            onBack = {},
            onDelete = {}
        )
    }
}

private object PreviewIdleHabitEventRecordSaveAction : AsyncAction {
    override val isRunning = false
    override fun invoke() = Unit
}
