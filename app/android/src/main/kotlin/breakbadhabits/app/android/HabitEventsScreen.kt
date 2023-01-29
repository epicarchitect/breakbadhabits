package breakbadhabits.app.android

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun HabitEventsScreen(
//    habitId: Int,
//    openHabitEventEditing: (habitEventId: Int) -> Unit,
//) {
//    val dateTimeFormatter: DateTimeFormatter = appDependencies.dateTimeFormatter
//    val habitIconResources: HabitIconResources =appDependencies.habitIconResources
//
//    val habitNameFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitNameFeature(habitId)
//    }
//    val habitIconIdFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitIconIdFeature(habitId)
//    }
//    val eventIdsFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitEventIdsFeature(habitId)
//    }
//    val eventTimesFeature = rememberEpicStoreEntry {
//        appDependencies.createHabitEventTimesFeature(habitId)
//    }
//    val habitName by habitNameFeature.state.collectAsState()
//    val habitIconId by habitIconIdFeature.state.collectAsState()
//    val eventIds by eventIdsFeature.state.collectAsState()
//    val eventTimes by eventTimesFeature.state.collectAsState()
//
//    val calendarState = rememberEventsCalendarState()
//
//    LaunchedEffect(calendarState.value) {
//        eventIdsFeature.setTimeFilter {
//            Instant.ofEpochMilli(it)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate()
//                .monthValue == calendarState.value.monthValue
//        }
//    }
//
//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(vertical = 32.dp)
//    ) {
//        item {
//            Column(
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Icon(
//                    modifier = Modifier
//                        .size(44.dp)
//                        .align(Alignment.CenterHorizontally),
//                    painter = painterResource(habitIconResources[habitIconId ?: 0])
//                )
//
//                Title(
//                    modifier = Modifier
//                        .padding(top = 8.dp)
//                        .align(Alignment.CenterHorizontally),
//                    text = habitName ?: ""
//                )
//            }
//        }
//
//        item {
//            EventsCalendar(
//                modifier = Modifier.padding(horizontal = 8.dp),
//                calendarState = calendarState,
//                events = eventTimes
//            )
//        }
//
//        epicStoreItems(eventIds) { eventId ->
//            val timeFeature = rememberEpicStoreEntry("HabitEventTimeFeature:$eventId") {
//                appDependencies.createHabitEventTimeFeature(eventId)
//            }
//            val commentFeature = rememberEpicStoreEntry("HabitEventCommentFeature:$eventId") {
//                appDependencies.createHabitEventCommentFeature(eventId)
//            }
//            val time by timeFeature.state.collectAsState()
//            val comment by commentFeature.state.collectAsState()
//
//            Box(
//                modifier = Modifier
//                    .animateItemPlacement()
//                    .fillMaxWidth()
//                    .clickable {
//                        openHabitEventEditing(eventId)
//                    }
//            ) {
//                Column(
//                    modifier = Modifier.padding(
//                        start = 14.dp,
//                        end = 14.dp,
//                        top = 4.dp,
//                        bottom = 4.dp
//                    ),
//                ) {
//                    Text(
//                        modifier = Modifier.padding(2.dp),
//                        text = time?.let {
//                            dateTimeFormatter.formatDateTime(
//                                Calendar.getInstance().apply {
//                                    timeInMillis = it
//                                },
//                                withoutYear = true
//                            )
//                        } ?: "-"
//                    )
//
//                    Text(
//                        modifier = Modifier.padding(2.dp),
//                        text = comment ?: stringResource(R.string.habitEvents_noComment)
//                    )
//                }
//            }
//        }
//    }
//}