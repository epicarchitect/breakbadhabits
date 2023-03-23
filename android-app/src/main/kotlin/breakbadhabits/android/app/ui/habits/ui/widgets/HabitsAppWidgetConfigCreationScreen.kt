package breakbadhabits.android.app.ui.habits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.controller.ValidatedInputController
import breakbadhabits.foundation.uikit.button.InteractionType
import breakbadhabits.foundation.uikit.button.RequestButton
import breakbadhabits.foundation.uikit.effect.ClearFocusWhenKeyboardHiddenEffect
import breakbadhabits.foundation.uikit.text.Text
import breakbadhabits.foundation.uikit.text.TextFieldAdapter
import breakbadhabits.foundation.uikit.text.Title
import breakbadhabits.foundation.uikit.text.ValidatedInputField

@Composable
fun HabitsAppWidgetConfigCreationScreen(
    titleInputController: ValidatedInputController<HabitAppWidgetConfig.Title, Nothing>,
    creationController: RequestController
) {
    ClearFocusWhenKeyboardHiddenEffect()

    Column {
        Title(
            text = stringResource(R.string.habitsAppWidgetConfigCreation_title)
        )

        Text(
            text = stringResource(R.string.habitsAppWidgetConfigCreation_name_description)
        )

        ValidatedInputField(
            controller = titleInputController,
            label = stringResource(R.string.habitsAppWidgetConfigCreation_name),
            adapter = TextFieldAdapter(
                decodeInput = HabitAppWidgetConfig.Title::value,
                encodeInput = HabitAppWidgetConfig::Title,
                extractErrorMessage = { null }
            )
        )

        Spacer(modifier = Modifier.weight(1.0f))

        Spacer(modifier = Modifier.height(24.dp))

        RequestButton(
            modifier = Modifier.align(Alignment.End),
            requestController = creationController,
            text = stringResource(R.string.habitsAppWidgetConfigCreation_finish),
            interactionType = InteractionType.MAIN
        )
    }
}

//@Composable
//fun HabitsAppWidgetConfigCreationScreen(
//    appWidgetId: Int,
//    onFinished: () -> Unit
//) {
//    val creationFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetCreationFeature()
//    }
//    val titleInputFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetTitleInputFeature()
//    }
//    val habitSelectionFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetHabitIdsSelectionFeature()
//    }
//
//    val habitSelection by habitSelectionFeature.selection.collectAsState()
//    val title by titleInputFeature.input.collectAsState()
//    val savingAllowed = habitSelection.containsValue(true)
//
//    val focusManager = LocalFocusManager.current
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column {
//            Title(
//                modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigCreation_title)
//            )
//
//            Text(
//                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigCreation_name_description)
//            )
//
//            TextField(
//                modifier = Modifier
//                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
//                    .fillMaxWidth(),
//                value = title ?: "",
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(
//                    onDone = {
//                        focusManager.clearFocus()
//                    }
//                ),
//                onValueChange = titleInputFeature::changeInput,
//                label = stringResource(R.string.habitsAppWidgetConfigCreation_name)
//            )
//
//            Text(
//                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigCreation_habits_description)
//            )
//
//            LazyColumn(
//                contentPadding = PaddingValues(
//                    start = 16.dp,
//                    end = 16.dp,
//                    top = 16.dp,
//                    bottom = 160.dp
//                ),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                epicStoreItems(habitSelection.keys.toList()) { habitId ->
//                    HabitItem(
//                        habitId = habitId,
//                        checked = habitSelection[habitId]!!,
//                        onCheckedChange = {
//                            habitSelectionFeature.setChecked(habitId, it)
//                        }
//                    )
//                }
//            }
//        }
//
//        Button(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.BottomEnd),
//            onClick = {
//                creationFeature.startCreation(
//                    titleInputFeature.input.value,
//                    appWidgetId,
//                    habitSelection.filterValues { it }.keys
//                )
//                onFinished()
//            },
//            enabled = savingAllowed,
//            text = stringResource(R.string.habitsAppWidgetConfigCreation_finish),
//            interactionType = InteractionType.MAIN
//        )
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun LazyItemScope.HabitItem(
//    habitId: Int,
//    checked: Boolean,
//    onCheckedChange: (Boolean) -> Unit
//) {
//    val habitNameFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitNameFeature(habitId)
//    }
//    val habitName by habitNameFeature.state.collectAsState()
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .animateItemPlacement()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onCheckedChange(!checked) },
//        ) {
//            Row(
//                modifier = Modifier.padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Checkbox(checked, onCheckedChange)
//
//                Text(
//                    modifier = Modifier.padding(start = 4.dp),
//                    text = habitName ?: ""
//                )
//            }
//        }
//    }
//}