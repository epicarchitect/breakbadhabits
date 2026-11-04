package epicarchitect.breakbadhabits.screens.dashboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import epicarchitect.breakbadhabits.habits.HabitsRules
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.rememberPremiumIsActive
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.app_name
import epicarchitect.breakbadhabits.resources.dashboard_empty_hint
import epicarchitect.breakbadhabits.resources.dashboard_empty_title
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_action
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_description
import epicarchitect.breakbadhabits.resources.dashboard_habit_limit_title
import epicarchitect.breakbadhabits.resources.dashboard_new_add
import epicarchitect.breakbadhabits.resources.premium_active
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.screens.monetization.PremiumOfferHabitLimitKey
import epicarchitect.breakbadhabits.screens.monetization.rememberPremiumOfferState
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.settings.orderedBy
import epicarchitect.breakbadhabits.settings.rememberAppSettings
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AdaptiveContentWidth
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.PremiumOfferCard
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun DashboardScreen() {
    val state = rememberDashboardScreenState()
    val isPremium = rememberPremiumIsActive()
    val purchasesAvailable = Monetization.purchasesAvailable
    val settings = rememberAppSettings()
    val settingsDescription = stringResource(Res.string.settings)
    val premiumActiveDescription = stringResource(Res.string.premium_active)
    val profileDescription = if (isPremium) {
        "$settingsDescription. $premiumActiveDescription"
    } else {
        settingsDescription
    }
    val navBackStack = LocalNavBackStack.current
    val coroutineScope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    val lazyGridState = rememberLazyGridState()
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val isWide = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val layoutDirection = LocalLayoutDirection.current
    val safeDrawingInsets = WindowInsets.safeDrawing.asPaddingValues()
    val safeStart = safeDrawingInsets.calculateStartPadding(layoutDirection)
    val safeEnd = safeDrawingInsets.calculateEndPadding(layoutDirection)
    val addHabitDescription = stringResource(Res.string.dashboard_new_add)
    val baseHabits = state?.habits.orEmpty()
        .orderedBy(settings.habitOrder) { it.habit.id.toString() }
    val showEmptyState = state != null && baseHabits.isEmpty()
    var habits by remember { mutableStateOf(baseHabits) }
    var orderBeforeDrag by remember { mutableStateOf(baseHabits) }
    var suppressClick by remember { mutableStateOf(false) }
    var showHabitLimitDialog by remember { mutableStateOf(false) }
    val habitLimitReached = !isPremium &&
            purchasesAvailable &&
            habits.size >= HabitsRules.freeHabitLimit
    val habitLimitOffer = rememberPremiumOfferState(PremiumOfferHabitLimitKey)
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

    val reorderableState = rememberReorderableLazyGridState(lazyGridState) { from, to ->
        val fromKey = from.key as? String
        val toKey = to.key as? String
        val fromIndex = habits.indexOfFirst { it.habit.id.toString() == fromKey }
        val toIndex = habits.indexOfFirst { it.habit.id.toString() == toKey }
        if (fromIndex >= 0 && toIndex >= 0) {
            habits = habits.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
        }
    }

    if (showHabitLimitDialog) {
        HabitLimitPremiumDialog(
            onDismiss = { showHabitLimitDialog = false },
            onOpenPremium = {
                showHabitLimitDialog = false
                navBackStack.push(RootRoute.Paywall)
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopBar(
            title = stringResource(Res.string.app_name),
            navigationIcon = {
                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .addHabitPulse(showEmptyState),
                    onClick = {
                        if (habitLimitReached) {
                            showHabitLimitDialog = true
                        } else {
                            navBackStack.push(RootRoute.HabitEditing(habitId = null))
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = addHabitDescription,
                        tint = if (showEmptyState) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            },
            actions = {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = { navBackStack.push(RootRoute.Settings) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = profileDescription,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )

        AdaptiveContent(
            modifier = Modifier.weight(1f),
            width = AdaptiveContentWidth.Wide
        ) {
            when {
                state == null -> Box(Modifier.fillMaxSize())

                showEmptyState -> DashboardEmptyState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 24.dp + safeStart,
                            end = 24.dp + safeEnd,
                            bottom = 24.dp + navigationBarBottom
                        )
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(if (isWide) 2 else 1),
                    modifier = Modifier.fillMaxSize(),
                    state = lazyGridState,
                    contentPadding = PaddingValues(
                        start = 16.dp + safeStart,
                        end = 16.dp + safeEnd,
                        bottom = 24.dp + navigationBarBottom
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    overscrollEffect = null
                ) {
                    if (habitLimitReached && habitLimitOffer.visible) {
                        item(
                            key = "habit_limit_offer",
                            span = { GridItemSpan(maxLineSpan) }
                        ) {
                            PremiumOfferCard(
                                modifier = Modifier.fillMaxWidth(),
                                title = stringResource(Res.string.dashboard_habit_limit_title),
                                description = stringResource(Res.string.dashboard_habit_limit_description),
                                actionText = stringResource(Res.string.dashboard_habit_limit_action),
                                onClick = {
                                    navBackStack.push(
                                        RootRoute.Paywall
                                    )
                                },
                                onDismiss = habitLimitOffer::dismiss
                            )
                        }
                    }

                    items(
                        items = habits,
                        key = { habit -> habit.habit.id.toString() }
                    ) { habit ->
                        val key = habit.habit.id.toString()
                        ReorderableItem(
                            state = reorderableState,
                            key = key
                        ) { isDragging ->
                            val interactionSource = remember { MutableInteractionSource() }
                            val scale by animateFloatAsState(
                                targetValue = if (isDragging) 1.025f else 1f,
                                label = "habitCardScale"
                            )
                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) 8.dp else 0.dp,
                                label = "habitCardElevation"
                            )
                            HabitCard(
                                habitWithLastEvent = habit,
                                interactionSource = interactionSource,
                                modifier = Modifier
                                    .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                    }
                                    .shadow(
                                        elevation = elevation,
                                        shape = MaterialTheme.shapes.medium
                                    ),
                                dragHandleModifier = Modifier.longPressDraggableHandle(
                                    interactionSource = interactionSource,
                                    onDragStarted = {
                                        suppressClick = true
                                        orderBeforeDrag = habits
                                        hapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                    },
                                    onDragStopped = {
                                        saveOrder()
                                        coroutineScope.launch {
                                            delay(150)
                                            suppressClick = false
                                        }
                                    }
                                ),
                                onClick = {
                                    if (suppressClick.not()) {
                                        navBackStack.push(
                                            RootRoute.HabitDetails(habit.habit.id)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardEmptyState(modifier: Modifier) {
    val glowColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier.drawBehind {
            val glowRadius = minOf(size.minDimension * 0.55f, 180.dp.toPx())
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.16f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = glowRadius
                ),
                radius = glowRadius,
                center = center
            )
        },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 320.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.dashboard_empty_title),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(Res.string.dashboard_empty_hint),
                modifier = Modifier.widthIn(max = 250.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Modifier.addHabitPulse(active: Boolean): Modifier {
    if (active.not()) return this

    val transition = rememberInfiniteTransition(label = "addHabitPulse")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2800,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "addHabitPulseProgress"
    )
    val color = MaterialTheme.colorScheme.primary

    return drawBehind {
        val startRadius = 16.dp.toPx()
        val endRadius = 26.dp.toPx()
        repeat(2) { index ->
            val wave = (progress + index * 0.5f) % 1f
            drawCircle(
                color = color.copy(alpha = (1f - wave) * 0.55f),
                radius = startRadius + (endRadius - startRadius) * wave,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}

@Preview(widthDp = 360, heightDp = 700, name = "Dashboard empty")
@Preview(widthDp = 320, heightDp = 700, fontScale = 2f, name = "Dashboard empty 2x")
@Composable
private fun DashboardEmptyStatePreview() {
    PreviewTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DashboardEmptyState(modifier = Modifier.fillMaxSize())
        }
    }
}
