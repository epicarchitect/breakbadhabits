package epicarchitect.breakbadhabits.screens.habits.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.controller.MultiSelectionController
import epicarchitect.breakbadhabits.foundation.controller.SingleRequestController
import epicarchitect.breakbadhabits.foundation.controller.ValidatedInputController
import epicarchitect.breakbadhabits.foundation.controller.toggleItem
import epicarchitect.breakbadhabits.foundation.uikit.Card
import epicarchitect.breakbadhabits.foundation.uikit.Checkbox
import epicarchitect.breakbadhabits.foundation.uikit.Dialog
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RequestButton
import epicarchitect.breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import epicarchitect.breakbadhabits.foundation.uikit.text.Text
import epicarchitect.breakbadhabits.foundation.uikit.text.TextFieldValidationAdapter
import epicarchitect.breakbadhabits.foundation.uikit.text.ValidatedTextField
import epicarchitect.breakbadhabits.logic.habits.model.Habit

@Composable
fun HabitAppWidgetUpdating(
    titleInputController: ValidatedInputController<String, Nothing>,
    habitsSelectionController: MultiSelectionController<Habit>,
    updatingController: SingleRequestController,
    deletionController: SingleRequestController
) {
    ClearFocusWhenKeyboardHiddenEffect()

    val habitsSelection by habitsSelectionController.state.collectAsState()

    var deletionShow by remember { mutableStateOf(false) }
    if (deletionShow) {
        Dialog(onDismiss = { deletionShow = false }) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "stringResource(R.string.habitsAppWidgets_deleteConfirmation)",
                    type = Text.Type.Description,
                    priority = Text.Priority.High
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Button(
                        text = "stringResource(R.string.cancel)",
                        onClick = {
                            deletionShow = false
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RequestButton(
                        controller = deletionController,
                        text = "stringResource(R.string.yes)",
                        type = Button.Type.Main
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = " stringResource(R.string.habitsAppWidgetConfigEditing_title)",
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "stringResource(R.string.habitsAppWidgetConfigEditing_name_description)"
        )

        Spacer(modifier = Modifier.height(12.dp))

        ValidatedTextField(
            controller = titleInputController,
            label = "Название",
            validationAdapter = TextFieldValidationAdapter { null }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "stringResource(R.string.habitsAppWidgetConfigEditing_habitsDescription)"
        )

        Spacer(modifier = Modifier.height(12.dp))

        when (val state = habitsSelection) {
            MultiSelectionController.State.Loading -> {}
            is MultiSelectionController.State.Loaded -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(state.items, key = { it.id }) { habit ->
                        HabitItem(
                            habit = habit,
                            checked = habit in state.selectedItems,
                            onClick = {
                                habitsSelectionController.toggleItem(habit)
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "stringResource(R.string.habitsAppWidgetConfigEditing_deletion_description)"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = "stringResource(R.string.habitsAppWidgetConfigEditing_deletion_button)",
            type = Button.Type.Dangerous,
            onClick = {
                deletionShow = true
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            controller = updatingController,
            text = "stringResource(R.string.habitsAppWidgetConfigEditing_finish)",
            type = Button.Type.Main
//            icon = {
//                LocalResourceIcon(resourceId = R.drawable.ic_done)
//            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitItem(
    habit: Habit,
    checked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
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
    }
}