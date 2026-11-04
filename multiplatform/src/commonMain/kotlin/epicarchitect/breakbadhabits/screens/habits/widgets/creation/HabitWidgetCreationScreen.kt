package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.habit_widget_add
import epicarchitect.breakbadhabits.resources.habit_widget_creation_title
import epicarchitect.breakbadhabits.resources.habit_widget_empty_habits
import epicarchitect.breakbadhabits.resources.habit_widget_habits_description
import epicarchitect.breakbadhabits.resources.habit_widget_habits_title
import epicarchitect.breakbadhabits.resources.habit_widget_save
import epicarchitect.breakbadhabits.uikit.InputCard
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

    val habitsState = remember {
        AppDatabase.habitsDao.habits()
    }.collectAsState(null)
    val habits = habitsState.value ?: return

    val selectedHabitIds = remember { mutableStateListOf<Uuid>() }
    var isEditing by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }
    var isLoaded by remember(systemWidgetId) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(systemWidgetId) {
        val existingWidget = AppDatabase.habitWidgetDao.widgetBySystemId(systemWidgetId)

        if (existingWidget != null) {
            val existingHabitIds = habits.mapTo(mutableSetOf()) { it.id }
            isEditing = true
            selectedHabitIds.clear()
            selectedHabitIds.addAll(existingWidget.habitIds.filter { it in existingHabitIds })
        }

        isLoaded = true
    }

    if (isLoaded.not()) {
        WidgetLoading()
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = {
                Text(
                    text = stringResource(Res.string.habit_widget_creation_title),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                IconButton(onClick = onCancel) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(Res.string.cancel)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            windowInsets = WindowInsets(0)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "habits") {
                InputCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.habit_widget_habits_title),
                    description = stringResource(Res.string.habit_widget_habits_description)
                ) {
                    if (habits.isEmpty()) {
                        Text(
                            text = stringResource(Res.string.habit_widget_empty_habits),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            habits.forEach { habit ->
                                HabitSelectionRow(
                                    habit = habit,
                                    selected = habit.id in selectedHabitIds,
                                    onClick = {
                                        if (habit.id in selectedHabitIds) {
                                            selectedHabitIds.remove(habit.id)
                                        } else {
                                            selectedHabitIds.add(habit.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = selectedHabitIds.isNotEmpty() && isSaving.not(),
                    onClick = {
                        if (isSaving) return@Button
                        isSaving = true
                        coroutineScope.launch {
                            try {
                                val existingWidget = AppDatabase.habitWidgetDao
                                    .widgetBySystemId(systemWidgetId)
                                AppDatabase.habitWidgetDao.save(
                                    HabitWidget(
                                        systemId = systemWidgetId,
                                        habitIds = selectedHabitIds.toList()
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
                ) {
                    Text(
                        text = stringResource(
                            if (isEditing) Res.string.habit_widget_save
                            else Res.string.habit_widget_add
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HabitSelectionRow(
    habit: Habit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            1.dp,
            if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                imageVector = HabitIcons.getById(habit.iconId).imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                text = habit.name,
                style = MaterialTheme.typography.bodyMedium
            )
            Checkbox(
                checked = selected,
                onCheckedChange = null
            )
        }
    }
}
