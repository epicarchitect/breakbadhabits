package breakbadhabits.app.ui.screen

//@Composable
//fun HabitsAppWidgetsScreen(
//    openHabitAppWidgetConfigEditing: (configId: Int) -> Unit
//) {
//    val habitsAppWidgetConfigIdsFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetConfigIdsFeature()
//    }
//
//    val configIds by habitsAppWidgetConfigIdsFeature.state.collectAsState()
//
//    Box(
//        modifier = Modifier.fillMaxSize()
//    ) {
//        if (configIds.isEmpty()) {
//            Text(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .padding(16.dp),
//                text = stringResource(R.string.habitsAppWidgets_empty),
//                textAlign = TextAlign.Center
//            )
//        } else {
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Title(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
//                    text = stringResource(R.string.main_widgets)
//                )
//
//                LazyColumn(
//                    contentPadding = PaddingValues(
//                        start = 16.dp,
//                        end = 16.dp,
//                        top = 16.dp,
//                        bottom = 160.dp
//                    ),
//                    verticalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    epicStoreItems(configIds) { configid ->
//                        WidgetConfigItem(
//                            configId = configid,
//                            onClick = {
//                                openHabitAppWidgetConfigEditing(configid)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun WidgetConfigItem(
//    configId: Int,
//    onClick: () -> Unit
//) {
//    val habitsAppWidgetTitleFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetTitleFeature(configId)
//    }
//
//    val habitsAppWidgetHabitIdsFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitsAppWidgetHabitIdsFeature(configId)
//    }
//
//    val title by habitsAppWidgetTitleFeature.state.collectAsState()
//    val habitIds by habitsAppWidgetHabitIdsFeature.state.collectAsState()
//
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .clickable {
//                    onClick()
//                }
//        ) {
//            Title(
//                modifier = Modifier.padding(
//                    start = 16.dp,
//                    top = 16.dp,
//                    end = 54.dp
//                ),
//                text = title.orEmpty().ifEmpty { "#$configId" }
//            )
//
//            Text(
//                modifier = Modifier.padding(
//                    start = 16.dp,
//                    bottom = 16.dp,
//                    end = 16.dp
//                ),
//                text = buildString {
//                    val list = habitIds?.toList() ?: emptyList()
//                    list.forEachIndexed { index, habitId ->
//                        val habitNameFeature = rememberEpicStoreEntry {
//                            appDependencies.createHabitNameFeature(habitId)
//                        }
//                        val habitName by habitNameFeature.state.collectAsState()
//                        append(habitName)
//                        if (index != list.lastIndex) {
//                            appendLine()
//                        }
//                    }
//                }
//            )
//        }
//    }
//}