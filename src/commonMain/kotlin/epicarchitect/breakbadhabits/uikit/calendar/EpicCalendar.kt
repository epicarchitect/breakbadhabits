package epicarchitect.breakbadhabits.uikit.calendar

//
//class SelectionCalendarState(
//    val epicState: EpicDatePickerState,
//    initialSelectedTimes: ClosedRange<LocalTime>
//) {
//    var showYearMonthSelection by mutableStateOf(false)
//    var selectedDateTimeTabIndex by mutableStateOf(0)
//    var selectedStartTime by mutableStateOf(initialSelectedTimes.start)
//    var selectedEndTime by mutableStateOf(initialSelectedTimes.endInclusive)
//}
//
//@Composable
//fun rememberSelectionCalendarState(
//    initialSelectedDateTime: ClosedRange<LocalDateTime>,
//    monthRange: ClosedRange<EpicMonth>
//): SelectionCalendarState {
//    val epicState = rememberEpicDatePickerState(
//        config = rememberEpicDatePickerConfig(
//            selectionContainerColor = AppTheme.colorScheme.primary,
//            selectionContentColor = AppTheme.colorScheme.onPrimary
//        ),
//        selectedDates = listOf(
//            initialSelectedDateTime.start.date,
//            initialSelectedDateTime.endInclusive.date
//        ),
//        monthRange = monthRange,
//        selectionMode = EpicDatePickerState.SelectionMode.Range
//    )
//    return remember(epicState, initialSelectedDateTime) {
//        SelectionCalendarState(
//            epicState = epicState,
//            initialSelectedTimes = initialSelectedDateTime.start.time..initialSelectedDateTime.endInclusive.time
//        )
//    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//val EpicCalendarPagerState.targetMonth get() = monthRange.getByIndex(pagerState.targetPage)
//
//@Composable
//fun RangeSelectionCalendarDialog(
//    state: SelectionCalendarState,
//    onCancel: () -> Unit,
//    onConfirm: (ClosedRange<LocalDateTime>) -> Unit
//) {
//    val coroutineScope = rememberCoroutineScope()
//    val strings = Environment.resources.strings.rangeSelectionCalendarDialogStrings
//
//    Dialog(
//        onDismiss = onCancel
//    ) {
//        Column {
//            AnimatedVisibility(visible = !state.showYearMonthSelection) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    IconButton(
//                        onClick = {
//                            coroutineScope.launch {
//                                state.epicState.pagerState.scrollMonths(-1)
//                            }
//                        },
//                        icon = Environment.resources.icons.commonIcons.arrowLeft
//                    )
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(40.dp)
//                            .weight(1f)
//                            .padding(horizontal = 4.dp)
//                            .clip(RoundedCornerShape(100.dp))
//                            .clickable {
//                                state.showYearMonthSelection = !state.showYearMonthSelection
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = state.epicState.pagerState.currentMonth.toMonthOfYear().formatted(),
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                    IconButton(
//                        onClick = {
//                            coroutineScope.launch {
//                                state.epicState.pagerState.scrollMonths(1)
//                            }
//                        },
//                        icon = Environment.resources.icons.commonIcons.arrowRight
//                    )
//                }
//            }
//            AnimatedVisibility(visible = state.showYearMonthSelection) {
//                var yearMonthSelectionBoxSize by remember { mutableStateOf(IntSize.Zero) }
//                Column(
//                    modifier = Modifier.onSizeChanged {
//                        yearMonthSelectionBoxSize = it
//                    }
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                state.showYearMonthSelection = false
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Icon(
//                            modifier = Modifier.padding(4.dp),
//                            icon = Environment.resources.icons.commonIcons.arrowUp
//                        )
//                    }
//
//                    ScrollableTabRow(
//                        selectedTabIndex = state.epicState.pagerState.targetMonth.month.ordinal,
//                        modifier = Modifier.fillMaxWidth(),
//                        containerColor = Color.Transparent,
//                        indicator = {},
//                        divider = {},
//                        edgePadding = 12.dp
//                    ) {
//                        repeat(Month.entries.size) {
//                            val month = Month.entries[it]
//                            val monthTitle = month.formatted()
//                            val isSelected = state.epicState.pagerState.targetMonth.month == month
//                            Card(
//                                modifier = Modifier.padding(4.dp),
//                                backgroundColor = if (isSelected) {
//                                    AppTheme.colorScheme.primary
//                                } else {
//                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
//                                },
//                                elevation = 0.dp
//                            ) {
//                                androidx.compose.material3.Text(
//                                    modifier = Modifier
//                                        .defaultMinSize(minWidth = 90.dp)
//                                        .clickable {
//                                            coroutineScope.launch {
//                                                state.epicState.pagerState.scrollToMonth(
//                                                    state.epicState.pagerState.targetMonth.copy(month = month)
//                                                )
//                                            }
//                                        }
//                                        .padding(
//                                            vertical = 8.dp,
//                                            horizontal = 20.dp
//                                        ),
//                                    text = monthTitle,
//                                    textAlign = TextAlign.Center,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontSize = 14.sp,
//                                    color = if (isSelected) {
//                                        AppTheme.colorScheme.onPrimary
//                                    } else {
//                                        AppTheme.colorScheme.onBackground
//                                    }
//                                )
//                            }
//                        }
//                    }
//                    val yearsCount = state.epicState.pagerState.monthRange.let {
//                        it.endInclusive.year - it.start.year
//                    }
//
//                    val endYear = state.epicState.pagerState.monthRange.endInclusive.year
//
//                    ScrollableTabRow(
//                        selectedTabIndex = yearsCount - (endYear - state.epicState.pagerState.targetMonth.year),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 8.dp),
//                        containerColor = Color.Transparent,
//                        indicator = {},
//                        divider = {},
//                        edgePadding = 12.dp
//                    ) {
//                        state.epicState.pagerState.monthRange.let {
//                            it.start.year..it.endInclusive.year
//                        }.forEach { year ->
//                            val yearTitle = year.toString()
//                            val isSelected = state.epicState.pagerState.targetMonth.year == year
//
//                            Card(
//                                modifier = Modifier.padding(4.dp),
//                                backgroundColor = if (isSelected) {
//                                    AppTheme.colorScheme.primary
//                                } else {
//                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
//                                },
//                                elevation = 0.dp,
//                            ) {
//                                androidx.compose.material3.Text(
//                                    modifier = Modifier
//                                        .defaultMinSize(minWidth = 90.dp)
//                                        .clickable {
//                                            coroutineScope.launch {
//                                                state.epicState.pagerState.scrollToMonth(
//                                                    state.epicState.pagerState.currentMonth.copy(year = year)
//                                                )
//                                            }
//                                        }
//                                        .padding(
//                                            vertical = 8.dp,
//                                            horizontal = 20.dp
//                                        ),
//                                    text = yearTitle,
//                                    textAlign = TextAlign.Center,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontSize = 14.sp,
//                                    color = if (isSelected) {
//                                        AppTheme.colorScheme.onPrimary
//                                    } else {
//                                        AppTheme.colorScheme.onBackground
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            EpicDatePicker(
//                modifier = Modifier.pointerInput(Unit) {
//                    detectVerticalDragGestures { change, _ ->
//                        if (change.previousPosition.y > change.position.y) {
//                            state.showYearMonthSelection = false
//                        }
//                    }
//                },
//                state = state.epicState
//            )
//
//            AnimatedVisibility(visible = !state.showYearMonthSelection && state.epicState.selectedDates.isNotEmpty()) {
//                Column {
//                    val selectedTime = if (state.selectedDateTimeTabIndex == 0) {
//                        state.selectedStartTime
//                    } else {
//                        state.selectedEndTime
//                    }
//
//                    ScrollableTabRow(
//                        selectedTabIndex = selectedTime.hour,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp),
//                        containerColor = Color.Transparent,
//                        indicator = {},
//                        divider = {},
//                        edgePadding = 12.dp
//                    ) {
//                        repeat(24) {
//                            val itemTime = LocalTime(it, 0, 0)
//                            val isSelected = selectedTime.hour == itemTime.hour
//                            Card(
//                                modifier = Modifier.padding(horizontal = 4.dp),
//                                backgroundColor = if (isSelected) {
//                                    AppTheme.colorScheme.primary
//                                } else {
//                                    AppTheme.colorScheme.onBackground.copy(alpha = 0.1f)
//                                },
//                                elevation = 0.dp,
//                            ) {
//                                androidx.compose.material3.Text(
//                                    modifier = Modifier
//                                        .defaultMinSize(minWidth = 90.dp)
//                                        .clickable {
//                                            if (state.selectedDateTimeTabIndex == 0) {
//                                                state.selectedStartTime = itemTime
//                                            } else {
//                                                state.selectedEndTime = itemTime
//                                            }
//                                        }
//                                        .padding(
//                                            vertical = 8.dp,
//                                            horizontal = 20.dp
//                                        ),
//                                    text = itemTime.formatted(),
//                                    textAlign = TextAlign.Center,
//                                    overflow = TextOverflow.Ellipsis,
//                                    fontSize = 14.sp,
//                                    color = if (isSelected) {
//                                        AppTheme.colorScheme.onPrimary
//                                    } else {
//                                        AppTheme.colorScheme.onBackground
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    TabRow(
//                        selectedTabIndex = state.selectedDateTimeTabIndex,
//                        containerColor = Color.Transparent
//                    ) {
//                        Tab(
//                            selected = state.selectedDateTimeTabIndex == 0,
//                            onClick = {
//                                coroutineScope.launch {
//                                    state.selectedDateTimeTabIndex = 0
//                                    state.epicState.selectedDates.minOrNull()?.let {
//                                        state.epicState.pagerState.scrollToMonth(it.epicMonth)
//                                    }
//                                }
//                            }
//                        ) {
//                            Column(
//                                modifier = Modifier.padding(8.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                androidx.compose.material3.Text(
//                                    text = strings.start(),
//                                    fontWeight = FontWeight.Bold,
//                                    color = AppTheme.colorScheme.onSurface
//                                )
//
//                                androidx.compose.material3.Text(
//                                    text = state.epicState.selectedDates.minOrNull()?.let {
//                                        val date = it.formatted()
//                                        val time = state.selectedStartTime.formatted()
//                                        "$date\n$time"
//                                    } ?: "",
//                                    fontWeight = FontWeight.Light,
//                                    fontSize = 12.sp,
//                                    style = TextStyle(
//                                        lineHeight = 12.sp
//                                    ),
//                                    textAlign = TextAlign.Center,
//                                    color = AppTheme.colorScheme.onSurface
//                                )
//                            }
//                        }
//                        Tab(
//                            selected = state.selectedDateTimeTabIndex == 1,
//                            onClick = {
//                                coroutineScope.launch {
//                                    state.selectedDateTimeTabIndex = 1
//                                    state.epicState.selectedDates.maxOrNull()?.let {
//                                        state.epicState.pagerState.scrollToMonth(it.epicMonth)
//                                    }
//                                }
//                            }
//                        ) {
//                            Column(
//                                modifier = Modifier.padding(8.dp),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                androidx.compose.material3.Text(
//                                    text = strings.end(),
//                                    fontWeight = FontWeight.Bold,
//                                    color = AppTheme.colorScheme.onSurface
//                                )
//
//                                androidx.compose.material3.Text(
//                                    text = state.epicState.selectedDates.maxOrNull()?.let {
//                                        val date = it.formatted()
//                                        val time = state.selectedEndTime.formatted()
//                                        "$date\n$time"
//                                    } ?: "",
//                                    fontWeight = FontWeight.Light,
//                                    fontSize = 12.sp,
//                                    style = TextStyle(
//                                        lineHeight = 12.sp
//                                    ),
//                                    textAlign = TextAlign.Center,
//                                    color = AppTheme.colorScheme.onSurface
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
//            ) {
//                androidx.compose.material3.Button(
//                    onClick = {
//                        if (state.showYearMonthSelection) {
//                            state.showYearMonthSelection = false
//                        } else {
//                            onCancel()
//                        }
//                    },
//                    elevation = ButtonDefaults.buttonElevation(0.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = AppTheme.colorScheme.surface,
//                        contentColor = AppTheme.colorScheme.onSurface
//                    ),
//                ) {
//                    androidx.compose.material3.Text(strings.cancel())
//                }
//                Spacer(Modifier.weight(1f))
//                AnimatedVisibility(visible = !state.showYearMonthSelection && state.epicState.selectedDates.isNotEmpty()) {
//                    androidx.compose.material3.Button(
//                        onClick = {
//                            val range = LocalDateTime(
//                                date = state.epicState.selectedDates.min(),
//                                time = state.selectedStartTime
//                            )..LocalDateTime(
//                                date = state.epicState.selectedDates.max(),
//                                time = state.selectedEndTime
//                            )
//                            onConfirm(range.ascended())
//                        },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = AppTheme.colorScheme.surface,
//                            contentColor = AppTheme.colorScheme.primary
//                        ),
//                        elevation = ButtonDefaults.buttonElevation(0.dp)
//                    ) {
//                        androidx.compose.material3.Text(strings.apply())
//                    }
//                }
//            }
//        }
//    }
//}