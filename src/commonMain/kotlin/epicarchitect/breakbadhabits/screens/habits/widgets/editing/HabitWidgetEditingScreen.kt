package epicarchitect.breakbadhabits.screens.habits.widgets.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.Checkbox
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.SimpleTopAppBar
import epicarchitect.breakbadhabits.uikit.button.Button
import epicarchitect.breakbadhabits.uikit.button.ButtonStyles
import epicarchitect.breakbadhabits.uikit.stateOfList
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.text.InputCard
import epicarchitect.breakbadhabits.uikit.text.Text
import epicarchitect.breakbadhabits.uikit.text.TextInputCard
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitWidget

class HabitWidgetEditingScreen(private val widgetId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitWidgetEditing(widgetId)
    }
}

@Composable
fun HabitWidgetEditing(widgetId: Int) {
    val strings = Environment.resources.strings.habitWidgetEditingStrings
    val navigator = LocalNavigator.currentOrThrow

    FlowStateContainer(
        state1 = stateOfOneOrNull { Environment.database.habitWidgetQueries.widgetById(widgetId) },
        state2 = stateOfList { Environment.database.habitQueries.habits() }
    ) { widget, habits ->
        Column {
            SimpleTopAppBar(title = strings.title(), onBackClick = navigator::pop)
            if (widget != null) {
                Content(widget, habits)
            }
        }
    }
}

@Composable
private fun ColumnScope.Content(
    initialWidget: HabitWidget,
    habits: List<Habit>,
) {
    val navigator = LocalNavigator.currentOrThrow
    val strings = Environment.resources.strings.habitWidgetEditingStrings
    val habitWidgetQueries = Environment.database.habitWidgetQueries

    val selectedHabitIds = remember(initialWidget) { initialWidget.habitIds.toMutableStateList() }
    var widgetTitle by rememberSaveable(initialWidget) { mutableStateOf(initialWidget.title) }

    var showDeletion by remember { mutableStateOf(false) }
    if (showDeletion) {
        DeletionDialog(
            widget = initialWidget,
            onDismiss = { showDeletion = false }
        )
    }

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
        title = strings.habitsTitle(),
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
        modifier = Modifier
            .padding(horizontal = 16.dp),
        text = strings.deleteButtonText(),
        style = ButtonStyles.dangerous,
        onClick = {
            showDeletion = true
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .align(Alignment.End),
        text = strings.finishButton(),
        style = ButtonStyles.primary,
        onClick = {
            habitWidgetQueries.update(
                id = initialWidget.id,
                title = widgetTitle,
                habitIds = selectedHabitIds,
            )
            navigator.pop()
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked,
            onCheckedChange = { onClick() }
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = habit.name
        )
    }
}

@Composable
private fun DeletionDialog(
    widget: HabitWidget,
    onDismiss: () -> Unit
) {
    val strings = Environment.resources.strings.habitWidgetEditingStrings
    val habitWidgetQueries = Environment.database.habitWidgetQueries

    Dialog(onDismiss = onDismiss) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = strings.deleteConfirmation(),
                type = Text.Type.Description,
                priority = Text.Priority.High
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    text = strings.cancel(),
                    onClick = onDismiss
                )

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    text = strings.yes(),
                    style = ButtonStyles.primary,
                    onClick = { habitWidgetQueries.deleteById(widget.id) }
                )
            }
        }
    }
}