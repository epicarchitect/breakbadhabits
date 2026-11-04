package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.border
import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.monetization.rememberPremiumIsActive
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.habit_widget_add
import epicarchitect.breakbadhabits.resources.habit_widget_creation_title
import epicarchitect.breakbadhabits.resources.habit_widget_empty_habits
import epicarchitect.breakbadhabits.resources.habit_widget_save_changes
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AppIconTile
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.AsyncAction
import epicarchitect.breakbadhabits.uikit.AsyncPrimaryButton
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.rememberAsyncAction
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitWidgetCreationScreen(
    systemWidgetId: Int,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    onOpenPremium: () -> Unit
) {
    if (!rememberPremiumIsActive()) {
        WidgetPremiumScreen(
            onBack = onCancel,
            onOpenPremium = onOpenPremium
        )
        return
    }

    val habitsWithLastEvent by remember {
        AppDatabase.habitsDao.habitsWithLastEvent()
    }.collectAsState(null)
    val selectedHabitIds = remember { mutableStateListOf<Uuid>() }
    var isEditing by remember { mutableStateOf(false) }
    var isLoaded by remember(systemWidgetId) { mutableStateOf(false) }
    val saveAction = rememberAsyncAction {
        val existingWidget = AppDatabase.habitWidgetDao
            .widgetBySystemId(systemWidgetId)
        AppDatabase.habitWidgetDao.save(
            HabitWidget(
                systemId = systemWidgetId,
                habitIds = habitsWithLastEvent
                    ?.map { it.habit.id }
                    ?.filter { it in selectedHabitIds }
                    .orEmpty()
            ).apply {
                id = existingWidget?.id ?: 0
            }
        )
        onDone()
    }

    LaunchedEffect(systemWidgetId, habitsWithLastEvent) {
        val habits = habitsWithLastEvent ?: return@LaunchedEffect
        if (isLoaded) return@LaunchedEffect

        val existingWidget = AppDatabase.habitWidgetDao.widgetBySystemId(systemWidgetId)
        if (existingWidget != null) {
            val existingHabitIds = habits.mapTo(mutableSetOf()) { it.habit.id }
            isEditing = true
            selectedHabitIds.clear()
            selectedHabitIds.addAll(existingWidget.habitIds.filter { it in existingHabitIds })
        }
        isLoaded = true
    }

    val habits = habitsWithLastEvent
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            bottom = navigationBarBottom
        ),
        state = listState
    ) {
        item(key = "top_bar") {
            AppTopBar(
                title = stringResource(Res.string.habit_widget_creation_title),
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(Res.string.cancel)
                        )
                    }
                }
            )
        }

        if (isLoaded.not() || habits == null) {
            item(key = "loading") {
                WidgetCreationItem {
                    WidgetCreationLoading(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                }
            }
        } else {
            if (habits.isEmpty()) {
                item(key = "empty_habits") {
                    WidgetCreationItem(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp
                        )
                    ) {
                        EmptyHabitsCard()
                    }
                }
            } else {
                items(
                    items = habits,
                    key = { habitWithLastEvent -> habitWithLastEvent.habit.id }
                ) { habitWithLastEvent ->
                    WidgetCreationItem {
                        HabitSelectionRow(
                            habitWithLastEvent = habitWithLastEvent,
                            selected = habitWithLastEvent.habit.id in selectedHabitIds,
                            onToggle = {
                                val habitId = habitWithLastEvent.habit.id
                                if (habitId in selectedHabitIds) {
                                    selectedHabitIds.remove(habitId)
                                } else {
                                    selectedHabitIds.add(habitId)
                                }
                            }
                        )
                    }
                }
            }

            item(key = "cta") {
                WidgetCreationItem(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 24.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    )
                ) {
                    AsyncPrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        action = saveAction,
                        text = stringResource(
                            if (isEditing) Res.string.habit_widget_save_changes
                            else Res.string.habit_widget_add
                        ),
                        enabled = selectedHabitIds.isNotEmpty()
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetCreationItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AdaptiveContent {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
private fun WidgetCreationLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HabitSelectionRow(
    habitWithLastEvent: HabitWithLastEvent,
    selected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = selected,
                    role = Role.Checkbox,
                    onValueChange = { onToggle() }
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppIconTile(
                imageVector = HabitIcons.getById(habitWithLastEvent.habit.iconId).imageVector,
                selected = selected
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                text = habitWithLastEvent.habit.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            SelectionIndicator(selected = selected)
        }
    }
}

@Composable
private fun SelectionIndicator(selected: Boolean) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.Transparent
        },
        label = "widgetHabitSelectionBackground"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        },
        label = "widgetHabitSelectionBorder"
    )

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                modifier = Modifier.size(15.dp),
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun EmptyHabitsCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            text = stringResource(Res.string.habit_widget_empty_habits),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(widthDp = 360, heightDp = 460, name = "WidgetCreation components")
@Composable
private fun WidgetCreationComponentsPreview() {
    PreviewTheme {
        Column(Modifier.padding(16.dp)) {
            WidgetCreationLoading(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
            HabitSelectionRow(
                habitWithLastEvent = HabitWithLastEvent(
                    habit = Habit(Uuid.random(), "Курение", 0),
                    lastEventEndTime = null
                ),
                selected = true,
                onToggle = {}
            )
            EmptyHabitsCard()
            AsyncPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                action = PreviewIdleAction,
                text = "Добавить на виджет"
            )
            Spacer(Modifier.height(12.dp))
            AsyncPrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                action = PreviewRunningAction,
                text = "Сохранение"
            )
        }
    }
}

private object PreviewIdleAction : AsyncAction {
    override val isRunning = false
    override fun invoke() = Unit
}

private object PreviewRunningAction : AsyncAction {
    override val isRunning = true
    override fun invoke() = Unit
}

@Preview(widthDp = 80, heightDp = 80, name = "SelectionIndicator")
@Composable
private fun SelectionIndicatorPreview() {
    PreviewTheme {
        Row(Modifier.padding(16.dp)) {
            SelectionIndicator(selected = true)
            Spacer(Modifier.width(16.dp))
            SelectionIndicator(selected = false)
        }
    }
}
