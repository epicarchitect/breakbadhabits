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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.appDependencies
import breakbadhabits.android.compose.ui.Button
import breakbadhabits.android.compose.ui.Card
import breakbadhabits.android.compose.ui.Checkbox
import breakbadhabits.android.compose.ui.InteractionType
import breakbadhabits.android.compose.ui.Text
import breakbadhabits.android.compose.ui.TextField
import breakbadhabits.android.compose.ui.Title
import epicarchitect.epicstore.compose.epicStoreItems
import epicarchitect.epicstore.compose.rememberEpicStoreEntry

@Composable
fun HabitsAppWidgetConfigCreationScreen(
    appWidgetId: Int,
    onFinished: () -> Unit
) {
    val creationFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetCreationFeature()
    }
    val titleInputFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetTitleInputFeature()
    }
    val habitSelectionFeature = rememberEpicStoreEntry {
        appDependencies.createHabitsAppWidgetHabitIdsSelectionFeature()
    }

    val habitSelection by habitSelectionFeature.selection.collectAsState()
    val title by titleInputFeature.input.collectAsState()
    val savingAllowed = habitSelection.containsValue(true)

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Title(
                modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
                text = stringResource(R.string.habitsAppWidgetConfigCreation_title)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitsAppWidgetConfigCreation_name_description)
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
                onValueChange = titleInputFeature::changeInput,
                label = stringResource(R.string.habitsAppWidgetConfigCreation_name)
            )

            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = stringResource(R.string.habitsAppWidgetConfigCreation_habits_description)
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
                epicStoreItems(habitSelection.keys.toList()) { habitId ->
                    HabitItem(
                        habitId = habitId,
                        checked = habitSelection[habitId]!!,
                        onCheckedChange = {
                            habitSelectionFeature.setChecked(habitId, it)
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                creationFeature.startCreation(
                    titleInputFeature.input.value,
                    appWidgetId,
                    habitSelection.filterValues { it }.keys
                )
                onFinished()
            },
            enabled = savingAllowed,
            text = stringResource(R.string.habitsAppWidgetConfigCreation_finish),
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