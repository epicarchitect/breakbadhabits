package breakbadhabits.android.app.ui.habits

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