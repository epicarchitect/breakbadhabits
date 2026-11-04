package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.HabitIcons
import epicarchitect.breakbadhabits.habits.habitProgress
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.monetization.rememberPremiumIsActive
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.abstinence_chart_open
import epicarchitect.breakbadhabits.resources.abstinence_chart_premium_description
import epicarchitect.breakbadhabits.resources.abstinence_chart_title
import epicarchitect.breakbadhabits.resources.habit_details_rustore_more_features
import epicarchitect.breakbadhabits.resources.risk_map_description
import epicarchitect.breakbadhabits.resources.risk_map_open
import epicarchitect.breakbadhabits.resources.risk_map_title
import epicarchitect.breakbadhabits.resources.settings
import epicarchitect.breakbadhabits.screens.monetization.PremiumOfferHistogramKey
import epicarchitect.breakbadhabits.screens.monetization.PremiumOfferRiskMapKey
import epicarchitect.breakbadhabits.screens.monetization.rememberPremiumOfferState
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.PremiumOfferCard
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import org.jetbrains.compose.resources.stringResource
import kotlin.uuid.Uuid

@Composable
fun HabitDetailsScreen(habitId: Uuid) {
    val state = rememberHabitDetailsScreenState(habitId) ?: return
    val isPremium = rememberPremiumIsActive()
    val purchasesAvailable = Monetization.purchasesAvailable
    val showPremiumFeatures = isPremium || purchasesAvailable
    val riskMapOffer = rememberPremiumOfferState(PremiumOfferRiskMapKey)
    val histogramOffer = rememberPremiumOfferState(PremiumOfferHistogramKey)
    val showRuStoreVersionNote = Monetization.store == PurchaseStore.GooglePlay &&
            !isPremium &&
            !purchasesAvailable
    val navBackStack = LocalNavBackStack.current
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()

    if (state.habitEventRecords.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
            HabitDetailsTopBar(
                title = state.habit.name,
                titleIcon = HabitIcons.getById(state.habit.iconId).imageVector,
                onBack = { navBackStack.removeLastOrNull() },
                onEdit = { navBackStack.push(RootRoute.HabitEditing(habitId)) }
            )

            AdaptiveContent(
                modifier = Modifier.weight(1f)
            ) {
                HabitDetailsEmptyState(
                    modifier = Modifier.fillMaxSize(),
                    onRecordSlip = {
                        navBackStack.push(
                            RootRoute.HabitEventRecordEditing(
                                habitEventRecordId = null,
                                habitId = state.habit.id
                            )
                        )
                    }
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            bottom = navigationBarBottom
        ),
        state = listState
    ) {
        item(key = "top_bar") {
            HabitDetailsTopBar(
                title = state.habit.name,
                titleIcon = HabitIcons.getById(state.habit.iconId).imageVector,
                onBack = { navBackStack.removeLastOrNull() },
                onEdit = { navBackStack.push(RootRoute.HabitEditing(habitId)) }
            )
        }

        item(key = "content") {
            AdaptiveContent {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.height(12.dp))

                    state.abstinence?.let { abstinence ->
                        HabitStageCard(
                            progress = habitProgress(abstinence),
                            duration = abstinence,
                            onRecordSlip = {
                                navBackStack.push(
                                    RootRoute.HabitEventRecordEditing(
                                        habitEventRecordId = null,
                                        habitId = state.habit.id
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    if (state.habitEventRecords.isNotEmpty()) {
                        HabitDetailsCalendarCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            state = state
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    if (showPremiumFeatures) {
                        state.riskMap?.let { riskMap ->
                            if (isPremium) {
                                HabitDetailsRiskMapCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    riskMap = riskMap
                                )
                                Spacer(Modifier.height(12.dp))
                            } else if (riskMapOffer.visible) {
                                PremiumOfferCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    title = stringResource(Res.string.risk_map_title),
                                    description = stringResource(Res.string.risk_map_description),
                                    actionText = stringResource(Res.string.risk_map_open),
                                    onClick = {
                                        navBackStack.push(
                                            RootRoute.Paywall
                                        )
                                    },
                                    onDismiss = riskMapOffer::dismiss
                                )
                                Spacer(Modifier.height(12.dp))
                            }
                        }
                    }

                    if (showPremiumFeatures &&
                        state.abstinenceHistogramValues.size > 2
                    ) {
                        if (isPremium) {
                            HabitDetailsHistogramCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                state = state
                            )
                            Spacer(Modifier.height(12.dp))
                        } else if (histogramOffer.visible) {
                            PremiumOfferCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                title = stringResource(Res.string.abstinence_chart_title),
                                description = stringResource(Res.string.abstinence_chart_premium_description),
                                actionText = stringResource(Res.string.abstinence_chart_open),
                                onClick = {
                                    navBackStack.push(
                                        RootRoute.Paywall
                                    )
                                },
                                onDismiss = histogramOffer::dismiss
                            )
                            Spacer(Modifier.height(12.dp))
                        }
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
private fun HabitDetailsTopBar(
    title: String,
    titleIcon: ImageVector,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    AppTopBar(
        title = title,
        titleIcon = titleIcon,
        navigationIcon = {
            AppBackButton(onClick = onBack)
        },
        actions = {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        }
    )
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
