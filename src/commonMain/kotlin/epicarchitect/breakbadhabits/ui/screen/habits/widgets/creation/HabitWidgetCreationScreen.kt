package epicarchitect.breakbadhabits.ui.screen.habits.widgets.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.environment.database.Habit
import epicarchitect.breakbadhabits.ui.component.Checkbox
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.SimpleTopAppBar
import epicarchitect.breakbadhabits.ui.component.button.Button
import epicarchitect.breakbadhabits.ui.component.button.ButtonStyles
import epicarchitect.breakbadhabits.ui.component.stateOfList
import epicarchitect.breakbadhabits.ui.component.text.InputCard
import epicarchitect.breakbadhabits.ui.component.text.Text
import epicarchitect.breakbadhabits.ui.component.text.TextInputCard

@Composable
fun HabitWidgetCreation(
    systemWidgetId: Int,
    onDone: () -> Unit
) {
    val appStrings by Environment.resources.strings.state.collectAsState()
    val strings = appStrings.habitWidgetCreationStrings
    val habitQueries = Environment.database.habitQueries

    FlowStateContainer(
        state = stateOfList { habitQueries.habits() }
    ) {
        Column {
            SimpleTopAppBar(title = strings.title())
            Content(
                habits = it,
                systemWidgetId = systemWidgetId,
                onDone = onDone
            )
        }
    }
}

@Composable
private fun ColumnScope.Content(
    habits: List<Habit>,
    systemWidgetId: Int,
    onDone: () -> Unit
) {
    val appStrings by Environment.resources.strings.state.collectAsState()
    val strings = appStrings.habitWidgetCreationStrings
    val habitWidgetQueries = Environment.database.habitWidgetQueries

    val selectedHabitIds = remember { mutableStateListOf<Int>() }
    var widgetTitle by rememberSaveable { mutableStateOf("") }

    Spacer(modifier = Modifier.height(16.dp))

    TextInputCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        title = strings.nameTitle(),
        description = strings.nameDescription(),
        value = widgetTitle,
        onValueChange = {
            widgetTitle = it
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    InputCard(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false)
            .padding(horizontal = 16.dp),
        title = "Habits",
        description = strings.habitsDescription()
    ) {
        LazyColumn {
            items(habits, key = { it.id }) { habit ->
                HabitItem(
                    habit = habit,
                    checked = selectedHabitIds.contains(habit.id),
                    onClick = {
                        if (selectedHabitIds.contains(habit.id)) {
                            selectedHabitIds.remove(habit.id)
                        } else {
                            selectedHabitIds.add(habit.id)
                        }
                    }
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.End),
        text = strings.finishButton(),
        style = ButtonStyles.primary,
        onClick = {
            habitWidgetQueries.insert(
                title = widgetTitle,
                habitIds = selectedHabitIds,
                systemId = systemWidgetId
            )
            onDone()
        }
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun HabitItem(
    habit: Habit,
    checked: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onClick() }
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = habit.name
        )
    }
}