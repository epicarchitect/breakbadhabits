package breakbadhabits.ui.kit

//@Composable
//fun EventsCalendar(
//    modifier: Modifier = Modifier,
//    calendarState: MutableState<YearMonth>,
//    events: List<Long>,
//    canChangeMonth: Boolean = true,
//    withHeader: Boolean = true,
//    horizontalSwipeEnabled: Boolean = true
//) {
//    val staticCalendarState = rememberCalendarState(calendarState.value)
//    calendarState.value = staticCalendarState.monthState.currentMonth
//
//
//    StaticCalendar(
//        modifier = modifier,
//        calendarState = staticCalendarState,
//        horizontalSwipeEnabled = horizontalSwipeEnabled,
//        monthHeader = { state ->
//            if (withHeader) {
//                Box(
//                    modifier = Modifier.padding(bottom = 16.dp),
//                ) {
//                    if (canChangeMonth) {
//                        IconButton(
//                            modifier = Modifier.align(Alignment.CenterStart),
//                            onClick = {
//                                state.currentMonth = state.currentMonth.minusMonths(1)
//                            }
//                        ) {
//                            Icon(Icons.Default.KeyboardArrowLeft)
//                        }
//
//
//                        IconButton(
//                            modifier = Modifier.align(Alignment.CenterEnd),
//                            onClick = {
//                                state.currentMonth = state.currentMonth.plusMonths(1)
//                            }
//                        ) {
//                            Icon(Icons.Default.KeyboardArrowRight)
//                        }
//                    }
//
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(44.dp),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = state.currentMonth.month
//                                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault())
//                                .lowercase()
//                                .replaceFirstChar { it.titlecase() },
//                            style = MaterialTheme.typography.body1,
//                        )
//
//                        Spacer(modifier = Modifier.width(8.dp))
//
//                        Text(
//                            text = state.currentMonth.year.toString(),
//                            style = MaterialTheme.typography.body1
//                        )
//                    }
//                }
//            }
//        },
//        dayContent = { state ->
//            val eventsCount = events.count {
//                val itemDate = Instant.ofEpochMilli(it)
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate()
//
//                itemDate.dayOfMonth == state.date.dayOfMonth
//                        && itemDate.monthValue == state.date.monthValue
//                        && itemDate.year == state.date.year
//            }
//
//            Box(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Card(
//                    modifier = Modifier
//                        .size(44.dp)
//                        .padding(4.dp)
//                        .align(Alignment.Center),
//                    shape = RoundedCornerShape(100.dp),
//                    elevation = 0.dp
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .align(Alignment.Center)
//                            .alpha(if (state.isFromCurrentMonth) 1.0f else 0.4f)
//                            .background(
//                                if (eventsCount > 0) {
//                                    MaterialTheme.colors.primary
//                                } else {
//                                    Color.Transparent
//                                }
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = state.date.dayOfMonth.toString(),
//                            color = if (eventsCount > 0) {
//                                Color.White
//                            } else {
//                                MaterialTheme.colors.onBackground
//                            }
//                        )
//                    }
//                }
//            }
//
//            if (eventsCount > 1) {
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Card(
//                        modifier = Modifier
//                            .padding(start = 32.dp, top = 2.dp)
//                            .defaultMinSize(16.dp)
//                            .align(Alignment.TopCenter),
//                        shape = RoundedCornerShape(100.dp),
//                        elevation = 4.dp,
//                        backgroundColor = Color.White
//                    ) {
//                        Box(
//                            modifier = Modifier.padding(2.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = if (eventsCount > 99) {
//                                    "99+"
//                                } else {
//                                    eventsCount.toString()
//                                },
//                                color = Color.Black,
//                                fontSize = 8.sp
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun rememberEventsCalendarState(
//    initialMonth: YearMonth = YearMonth.now()
//) = remember { mutableStateOf(initialMonth) }