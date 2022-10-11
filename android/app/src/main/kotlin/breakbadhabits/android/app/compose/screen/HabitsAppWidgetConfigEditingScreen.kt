package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.app.utils.AlertDialogManager
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.Checkbox
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.TextField
import breakbadhabits.android.compose.ui.Title
import epicarchitect.epicstore.compose.rememberEpicStoreEntry

@Composable
fun HabitsAppWidgetConfigEditingScreen(
    configId: Int,
    onFinished: () -> Unit
) {
    val alertDialogManager: AlertDialogManager = appDependencies.alertDialogManager
    val updatingFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetUpdatingFeature()
    }
    val deletionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetDeletionFeature()
    }
    val habitSelectionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetHabitIdsSelectionFeature(configId)
    }
    val titleFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetTitleInputFeature(configId)
    }
    val habitSelection by habitSelectionFeature.selection.collectAsState()
    val title by titleFeature.input.collectAsState()

    val savingAllowed = habitSelection.containsValue(true)
            && (habitSelectionFeature.initialValue.sorted() != habitSelection.filterValues { it }.keys.sorted()
            || titleFeature.initialInput != title)

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Title(
                modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
                text = stringResource(R.string.habitsAppWidgetConfigEditing_title)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitsAppWidgetConfigEditing_name_description)
            )

            TextField(
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                    .fillMaxWidth(),
                value = title ?: "",
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = titleFeature::changeInput,
                label = stringResource(R.string.habitsAppWidgetConfigEditing_name)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitsAppWidgetConfigEditing_habitsDescription)
            )

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 160.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(habitSelection.keys.toList()) { itemId ->
                    HabitItem(
                        habitId = itemId,
                        checked = habitSelection[itemId]!!,
                        onCheckedChange = {
                            habitSelectionFeature.setChecked(itemId, it)
                        }
                    )
                }

                item {
                    Column {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(R.string.habitsAppWidgetConfigEditing_deletion_description)
                        )

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = {
                                alertDialogManager.showAlert(
                                    context,
                                    title = null,
                                    message = context.getString(R.string.habitsAppWidgets_deleteConfirmation),
                                    positiveButtonTitle = context.getString(R.string.yes),
                                    negativeButtonTitle = context.getString(R.string.cancel),
                                    onPositive = {
                                        deletionFeature.startDeletion(configId)
                                        onFinished()
                                    },
                                )
                            },
                            text = stringResource(R.string.habitsAppWidgetConfigEditing_deletion_button),
                            interactionType = InteractionType.DANGEROUS
                        )
                    }
                }
            }
        }

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                updatingFeature.startUpdating(
                    configId,
                    title,
                    habitSelection.filterValues { it }.keys
                )
                onFinished()
            },
            enabled = savingAllowed,
            text = stringResource(R.string.habitsAppWidgetConfigEditing_finish),
            interactionType = InteractionType.MAIN
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyItemScope.HabitItem(
    habitId: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val habitNameFeature = rememberEpicStoreEntry {
        appDependencies.createHabitNameFeature(habitId)
    }
    val habitName by habitNameFeature.state.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateItemPlacement()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) },
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked, onCheckedChange)

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = habitName ?: ""
                )
            }
        }
    }
}