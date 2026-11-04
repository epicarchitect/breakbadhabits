package epicarchitect.breakbadhabits.screens.habits.widgets.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.done
import epicarchitect.breakbadhabits.resources.habit_widget_creation_title
import epicarchitect.breakbadhabits.resources.habit_widget_habits_description
import epicarchitect.breakbadhabits.resources.habit_widget_habits_title
import epicarchitect.breakbadhabits.resources.habit_widget_name_description
import epicarchitect.breakbadhabits.resources.habit_widget_name_title
import epicarchitect.breakbadhabits.uikit.InputCard
import epicarchitect.breakbadhabits.uikit.TextInputCard
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitWidgetCreationScreen(
    systemWidgetId: Int,
    onDone: () -> Unit
) {
    val habitsState = remember {
        AppDatabase.habitsDao.habits()
    }.collectAsState(null)
    val habits = habitsState.value ?: return

    val selectedHabitIds = remember { mutableStateListOf<Uuid>() }
    var widgetTitle by rememberSaveable { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var isLoaded by remember(systemWidgetId) { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(systemWidgetId) {
        val existingWidget = AppDatabase.habitWidgetDao.widgetBySystemId(systemWidgetId)

        if (existingWidget != null) {
            widgetTitle = existingWidget.title
            selectedHabitIds.clear()
            selectedHabitIds.addAll(existingWidget.habitIds)
        }

        isLoaded = true
    }

    if (isLoaded.not()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.habit_widget_creation_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                windowInsets = WindowInsets(0)
            )
        }

        item(key = "name") {
            Spacer(Modifier.height(16.dp))
            TextInputCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                value = widgetTitle,
                onValueChange = {
                    widgetTitle = it
                },
                title = stringResource(Res.string.habit_widget_name_title),
                description = stringResource(Res.string.habit_widget_name_description)
            )
        }

        item(key = "habits") {
            Spacer(Modifier.height(16.dp))
            InputCard(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                title = stringResource(Res.string.habit_widget_habits_title),
                description = stringResource(Res.string.habit_widget_habits_description)
            ) {
                Column {
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

        item(key = "done") {
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    enabled = selectedHabitIds.isNotEmpty() && isSaving.not(),
                    onClick = {
                        if (isSaving) return@Button
                        isSaving = true
                        coroutineScope.launch {
                            val existingWidget = AppDatabase.habitWidgetDao
                                .widgetBySystemId(systemWidgetId)
                            AppDatabase.habitWidgetDao.save(
                                HabitWidget(
                                    title = widgetTitle,
                                    systemId = systemWidgetId,
                                    habitIds = selectedHabitIds.toList()
                                ).apply {
                                    id = existingWidget?.id ?: 0
                                }
                            )
                            onDone()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.done)
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitSelectionRow(
    habit: Habit,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = {
                onClick()
            }
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = habit.name
        )
    }
}
