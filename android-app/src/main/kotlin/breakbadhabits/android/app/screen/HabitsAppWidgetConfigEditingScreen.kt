package breakbadhabits.android.app.screen

//@Composable
//fun HabitsAppWidgetConfigEditingScreen(
//    configId: Int,
//    onFinished: () -> Unit
//) {
//    val alertDialogManager: AlertDialogManager = appDependencies.alertDialogManager
//    val updatingFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetUpdatingFeature()
//    }
//    val deletionFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetDeletionFeature()
//    }
//    val habitSelectionFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetHabitIdsSelectionFeature(configId)
//    }
//    val titleFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetTitleInputFeature(configId)
//    }
//    val habitSelection by habitSelectionFeature.selection.collectAsState()
//    val title by titleFeature.input.collectAsState()
//
//    val savingAllowed = habitSelection.containsValue(true)
//            && (habitSelectionFeature.initialValue.sorted() != habitSelection.filterValues { it }.keys.sorted()
//            || titleFeature.initialInput != title)
//
//    val context = LocalContext.current
//    val focusManager = LocalFocusManager.current
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column {
//            Title(
//                modifier = Modifier.padding(start = 18.dp, top = 18.dp, end = 18.dp, bottom = 4.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigEditing_title)
//            )
//
//            Text(
//                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigEditing_name_description)
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
//                onValueChange = titleFeature::changeInput,
//                label = stringResource(R.string.habitsAppWidgetConfigEditing_name)
//            )
//
//            Text(
//                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                text = stringResource(R.string.habitsAppWidgetConfigEditing_habitsDescription)
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
//                items(habitSelection.keys.toList()) { itemId ->
//                    HabitItem(
//                        habitId = itemId,
//                        checked = habitSelection[itemId]!!,
//                        onCheckedChange = {
//                            habitSelectionFeature.setChecked(itemId, it)
//                        }
//                    )
//                }
//
//                item {
//                    Column {
//                        Text(
//                            modifier = Modifier.padding(top = 16.dp),
//                            text = stringResource(R.string.habitsAppWidgetConfigEditing_deletion_description)
//                        )
//
//                        Button(
//                            modifier = Modifier.padding(top = 8.dp),
//                            onClick = {
//                                alertDialogManager.showAlert(
//                                    context,
//                                    title = null,
//                                    message = context.getString(R.string.habitsAppWidgets_deleteConfirmation),
//                                    positiveButtonTitle = context.getString(R.string.yes),
//                                    negativeButtonTitle = context.getString(R.string.cancel),
//                                    onPositive = {
//                                        deletionFeature.startDeletion(configId)
//                                        onFinished()
//                                    },
//                                )
//                            },
//                            text = stringResource(R.string.habitsAppWidgetConfigEditing_deletion_button),
//                            interactionType = InteractionType.DANGEROUS
//                        )
//                    }
//                }
//            }
//        }
//
//        Button(
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.BottomEnd),
//            onClick = {
//                updatingFeature.startUpdating(
//                    configId,
//                    title,
//                    habitSelection.filterValues { it }.keys
//                )
//                onFinished()
//            },
//            enabled = savingAllowed,
//            text = stringResource(R.string.habitsAppWidgetConfigEditing_finish),
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