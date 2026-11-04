package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.dashboard_empty_title
import epicarchitect.breakbadhabits.resources.dashboard_new_add
import epicarchitect.breakbadhabits.resources.dashboard_today_title
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.settings.orderedBy
import epicarchitect.breakbadhabits.settings.rememberAppSettings
import epicarchitect.breakbadhabits.uikit.dragDropContainer
import epicarchitect.breakbadhabits.uikit.dragDropItem
import epicarchitect.breakbadhabits.uikit.dragDropVisuals
import epicarchitect.breakbadhabits.uikit.rememberDragDropMotion
import epicarchitect.breakbadhabits.uikit.rememberDragDropState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

private const val FREE_HABIT_LIMIT = 2
private const val HABIT_LIST_HEADER_ITEMS = 1

@Composable
fun DashboardScreen() {
    val state = rememberDashboardScreenState()
    val premium by Monetization.premiumCache.collectAsState()
    val isPremium = premium?.expiresAt?.let { it > Clock.System.now() } == true
    val settings = rememberAppSettings()
    val navBackStack = LocalNavBackStack.current
    val coroutineScope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()
    val motion = rememberDragDropMotion()
    val baseHabits = state?.habits.orEmpty()
        .orderedBy(settings.habitOrder) { it.habit.id.toString() }
    var habits by remember { mutableStateOf(baseHabits) }
    var orderBeforeDrag by remember { mutableStateOf(baseHabits) }
    var suppressClick by remember { mutableStateOf(false) }
    val dragDropState = rememberDragDropState(
        onMove = { fromIndex, toIndex ->
            val from = fromIndex - HABIT_LIST_HEADER_ITEMS
            val to = toIndex - HABIT_LIST_HEADER_ITEMS
            if (from in habits.indices && to in habits.indices) {
                habits = habits.toMutableList().apply {
                    add(to, removeAt(from))
                }
            }
        },
        lazyListState = lazyListState,
        motion = motion
    )

    LaunchedEffect(baseHabits) {
        habits = baseHabits
        orderBeforeDrag = baseHabits
    }

    fun saveOrder() {
        val newOrder = habits.map { it.habit.id.toString() }
        if (newOrder == orderBeforeDrag.map { it.habit.id.toString() }) return

        coroutineScope.launch {
            AppDatabase.appSettingsDao.save(
                settings.copy(habitOrder = newOrder)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .dragDropContainer(dragDropState),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(key = "app_bar") {
                TopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Text(
                            text = stringResource(Res.string.dashboard_today_title),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    actions = {
                        IconButton(onClick = { navBackStack.push(RootRoute.Settings) }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(Res.string.settings)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }

            if (habits.isEmpty()) {
                item(key = "empty_state") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.7f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(Res.string.dashboard_empty_title)
                        )
                    }
                }
            } else {
                itemsIndexed(
                    items = habits,
                    key = { _, habit -> habit.habit.id.toString() }
                ) { index, habit ->
                    val key = habit.habit.id.toString()
                    HabitCard(
                        habitWithLastEvent = habit,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .dragDropItem(
                                state = dragDropState,
                                key = key,
                                index = index + HABIT_LIST_HEADER_ITEMS,
                                onDragStarted = {
                                    suppressClick = true
                                    orderBeforeDrag = habits
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                },
                                onDragStopped = {
                                    saveOrder()
                                    coroutineScope.launch {
                                        delay(150)
                                        suppressClick = false
                                    }
                                }
                            )
                            .dragDropVisuals(
                                isDragging = dragDropState.isActive(key),
                                motion = motion
                            ),
                        onClick = {
                            if (suppressClick.not()) {
                                navBackStack.push(RootRoute.HabitDetails(habit.habit.id))
                            }
                        }
                    )
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(16.dp),
            onClick = {
                val canAdd = isPremium ||
                        habits.size < FREE_HABIT_LIMIT
                navBackStack.push(
                    if (canAdd) RootRoute.HabitEditing(habitId = null)
                    else RootRoute.Paywall(source = "habit_limit")
                )
            }
        ) {
            Text(text = stringResource(Res.string.dashboard_new_add))
        }
    }
}
