package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.habit_details_rustore_more_features
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppScaffold
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.rememberEnterAlwaysTopBarScrollBehavior
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Composable
fun HabitDetailsScreen(habitId: Uuid) {
    val state = rememberHabitDetailsScreenState(habitId) ?: return
    val premium by Monetization.premiumCache.collectAsState()
    val isPremium = premium?.expiresAt?.let { it > Clock.System.now() } == true
    val purchasesAvailable = Monetization.purchasesAvailable
    val showPremiumFeatures = isPremium || purchasesAvailable
    val showRuStoreVersionNote = Monetization.store == PurchaseStore.GooglePlay &&
            !isPremium &&
            !purchasesAvailable
    val navBackStack = LocalNavBackStack.current
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()
    val topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)

    AppScaffold(
        title = state.habit.name,
        topBarScrollBehavior = topBarScrollBehavior,
        navigationIcon = {
            AppBackButton(onClick = { navBackStack.removeLastOrNull() })
        },
        actions = {
            IconButton(onClick = { navBackStack.push(RootRoute.HabitEditing(habitId)) }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        }
    ) { topBarHeight ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = topBarHeight,
                bottom = navigationBarBottom
            ),
            state = listState
        ) {
            item(key = "content") {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(16.dp))
                    HabitDetailsHeaderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        state = state
                    )

                    Spacer(Modifier.height(16.dp))

                    state.abstinence?.let { duration ->
                        HabitStageCard(
                            progress = habitProgress(duration),
                            onAllStagesClick = {
                                navBackStack.push(RootRoute.HabitStages(habitId))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (showPremiumFeatures) {
                        state.riskMap?.let { riskMap ->
                            HabitDetailsRiskMapCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                riskMap = riskMap,
                                premium = isPremium,
                                onOpenPremium = {
                                    navBackStack.push(RootRoute.Paywall(source = "risk_map"))
                                }
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    if (state.habitEventRecords.isNotEmpty()) {
                        HabitDetailsCalendarCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (showPremiumFeatures &&
                        state.abstinenceHistogramValues.size > 2
                    ) {
                        HabitDetailsHistogramCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state,
                            premium = isPremium,
                            onOpenPremium = {
                                navBackStack.push(RootRoute.Paywall(source = "histogram"))
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (state.statistics.isNotEmpty()) {
                        HabitDetailsStatisticsCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    if (showRuStoreVersionNote) {
                        HabitDetailsRuStoreNote(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                }
            }
        }
    }
}

@Composable
private fun HabitDetailsRuStoreNote(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            text = stringResource(Res.string.habit_details_rustore_more_features),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(widthDp = 360, heightDp = 90, name = "HabitDetailsRuStoreNote")
@Composable
private fun HabitDetailsRuStoreNotePreview() {
    PreviewTheme {
        HabitDetailsRuStoreNote(
            modifier = Modifier.fillMaxWidth()
        )
    }
}
