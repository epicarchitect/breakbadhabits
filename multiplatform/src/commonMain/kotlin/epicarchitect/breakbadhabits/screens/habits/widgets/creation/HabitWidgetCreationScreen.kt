package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.habit_widget_add
import epicarchitect.breakbadhabits.resources.habit_widget_creation_title
import epicarchitect.breakbadhabits.resources.habit_widget_empty_habits
import epicarchitect.breakbadhabits.resources.habit_widget_save_changes
import epicarchitect.breakbadhabits.uikit.AppScaffold
import epicarchitect.breakbadhabits.uikit.rememberEnterAlwaysTopBarScrollBehavior
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Composable
fun HabitWidgetCreationScreen(
    systemWidgetId: Int,
    onDone: () -> Unit,
    onCancel: () -> Unit,
    onOpenPremium: () -> Unit
) {
    val premiumCache by remember {
        Monetization.premiumCache
    }.collectAsState()

    if (premiumCache?.expiresAt?.let { it > Clock.System.now() } != true) {
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
    var isSaving by remember { mutableStateOf(false) }
    var isLoaded by remember(systemWidgetId) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
    val topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)

    AppScaffold(
        title = stringResource(Res.string.habit_widget_creation_title),
        topBarScrollBehavior = topBarScrollBehavior,
        navigationIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(Res.string.cancel)
                )
            }
        }
    ) { topBarHeight ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = topBarHeight,
                bottom = navigationBarBottom
            ),
            state = listState
        ) {
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
                    WidgetCreationButton(
                        label = stringResource(
                            if (isEditing) Res.string.habit_widget_save_changes
                            else Res.string.habit_widget_add
                        ),
                        enabled = selectedHabitIds.isNotEmpty() && isSaving.not(),
                        isSaving = isSaving,
                        onClick = {
                            if (isSaving) return@WidgetCreationButton
                            isSaving = true
                            coroutineScope.launch {
                                try {
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
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    )
                }
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
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
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
    val iconContainerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        } else {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        },
        label = "widgetHabitIconBackground"
    )
    val iconColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "widgetHabitIconColor"
    )

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
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconContainerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = HabitIcons.getById(habitWithLastEvent.habit.iconId).imageVector,
                contentDescription = null,
                tint = iconColor
            )
        }
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            text = habitWithLastEvent.habit.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        SelectionIndicator(selected = selected)
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
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface
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

@Composable
private fun WidgetCreationButton(
    label: String,
    enabled: Boolean,
    isSaving: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f)
        )
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
            WidgetCreationButton(
                label = "Добавить на виджет",
                enabled = true,
                isSaving = false,
                onClick = {}
            )
            Spacer(Modifier.height(12.dp))
            WidgetCreationButton(
                label = "Сохранение",
                enabled = false,
                isSaving = true,
                onClick = {}
            )
        }
    }
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
