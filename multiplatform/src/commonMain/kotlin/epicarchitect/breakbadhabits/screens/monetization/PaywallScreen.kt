package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.paywall_analytics_calendar_insight
import epicarchitect.breakbadhabits.resources.paywall_analytics_calendar_legend
import epicarchitect.breakbadhabits.resources.paywall_analytics_calendar_period
import epicarchitect.breakbadhabits.resources.paywall_analytics_calendar_title
import epicarchitect.breakbadhabits.resources.paywall_analytics_cta_lifetime
import epicarchitect.breakbadhabits.resources.paywall_analytics_cta_month
import epicarchitect.breakbadhabits.resources.paywall_analytics_deck_action
import epicarchitect.breakbadhabits.resources.paywall_analytics_deck_title
import epicarchitect.breakbadhabits.resources.paywall_analytics_dynamics_change
import epicarchitect.breakbadhabits.resources.paywall_analytics_dynamics_headline
import epicarchitect.breakbadhabits.resources.paywall_analytics_dynamics_subhead
import epicarchitect.breakbadhabits.resources.paywall_analytics_dynamics_title
import epicarchitect.breakbadhabits.resources.paywall_analytics_eyebrow
import epicarchitect.breakbadhabits.resources.paywall_analytics_headline
import epicarchitect.breakbadhabits.resources.paywall_analytics_included
import epicarchitect.breakbadhabits.resources.paywall_analytics_module_calendar
import epicarchitect.breakbadhabits.resources.paywall_analytics_module_dynamics
import epicarchitect.breakbadhabits.resources.paywall_analytics_module_statistics
import epicarchitect.breakbadhabits.resources.paywall_analytics_module_weekly
import epicarchitect.breakbadhabits.resources.paywall_analytics_per_month
import epicarchitect.breakbadhabits.resources.paywall_analytics_per_year
import epicarchitect.breakbadhabits.resources.paywall_analytics_once
import epicarchitect.breakbadhabits.resources.paywall_analytics_plans_note
import epicarchitect.breakbadhabits.resources.paywall_analytics_plans_title
import epicarchitect.breakbadhabits.resources.paywall_analytics_show_month
import epicarchitect.breakbadhabits.resources.paywall_analytics_stat_best_streak
import epicarchitect.breakbadhabits.resources.paywall_analytics_stat_clean_days
import epicarchitect.breakbadhabits.resources.paywall_analytics_stat_metrics
import epicarchitect.breakbadhabits.resources.paywall_analytics_stat_recovery
import epicarchitect.breakbadhabits.resources.paywall_analytics_statistics_title
import epicarchitect.breakbadhabits.resources.paywall_analytics_subhead
import epicarchitect.breakbadhabits.resources.paywall_analytics_trial_badge
import epicarchitect.breakbadhabits.resources.paywall_analytics_trial_note
import epicarchitect.breakbadhabits.resources.paywall_cta_trial_days
import epicarchitect.breakbadhabits.resources.paywall_debug_stub
import epicarchitect.breakbadhabits.resources.paywall_googleplay_message
import epicarchitect.breakbadhabits.resources.paywall_googleplay_title
import epicarchitect.breakbadhabits.resources.paywall_ios_message
import epicarchitect.breakbadhabits.resources.paywall_ios_title
import epicarchitect.breakbadhabits.resources.paywall_lifetime_note
import epicarchitect.breakbadhabits.resources.paywall_loading
import epicarchitect.breakbadhabits.resources.paywall_manage_note
import epicarchitect.breakbadhabits.resources.paywall_offer_lifetime
import epicarchitect.breakbadhabits.resources.paywall_offer_month
import epicarchitect.breakbadhabits.resources.paywall_offer_year
import epicarchitect.breakbadhabits.resources.paywall_per_month
import epicarchitect.breakbadhabits.resources.paywall_plan_best
import epicarchitect.breakbadhabits.resources.paywall_plan_once
import epicarchitect.breakbadhabits.resources.paywall_price_per_month
import epicarchitect.breakbadhabits.resources.paywall_price_per_year
import epicarchitect.breakbadhabits.resources.paywall_premium_active_message
import epicarchitect.breakbadhabits.resources.paywall_premium_active_title
import epicarchitect.breakbadhabits.resources.paywall_privacy
import epicarchitect.breakbadhabits.resources.paywall_products_error
import epicarchitect.breakbadhabits.resources.paywall_purchase
import epicarchitect.breakbadhabits.resources.paywall_purchase_cancelled
import epicarchitect.breakbadhabits.resources.paywall_purchase_error
import epicarchitect.breakbadhabits.resources.paywall_renewal_note
import epicarchitect.breakbadhabits.resources.paywall_restore
import epicarchitect.breakbadhabits.resources.paywall_restore_error
import epicarchitect.breakbadhabits.resources.paywall_restore_nothing
import epicarchitect.breakbadhabits.resources.paywall_retry
import epicarchitect.breakbadhabits.resources.paywall_terms
import epicarchitect.breakbadhabits.resources.paywall_test_premium
import epicarchitect.breakbadhabits.resources.paywall_title
import epicarchitect.breakbadhabits.resources.paywall_unavailable_message
import epicarchitect.breakbadhabits.resources.paywall_unavailable_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.TERMS_OF_USE_URL
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

private val PrimaryPlanIds = listOf(
    PremiumProductId.Year,
    PremiumProductId.Lifetime
)

@Composable
fun PaywallScreen(source: String = "unknown") {
    val monetization = Monetization
    val state = rememberPaywallScreenState(source)
    val premiumCache by monetization.premiumCache.collectAsState()
    val isPremium = premiumCache?.expiresAt?.let { it > Clock.System.now() } == true
    val navBackStack = LocalNavBackStack.current
    var showMonthly by rememberSaveable { mutableStateOf(false) }

    val products = (state.productsState as? PaywallProductsState.Loaded)?.products.orEmpty()
    val selectedProduct = products.find { it.id == state.selectedProductId }
    val hasPurchaseCta = isPremium.not() &&
        state.purchasesAvailable &&
        selectedProduct != null
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.paywall_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navBackStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = 190.dp + bottomInset
                )
            ) {
                item(key = "content") {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    if (isPremium) {
                        PaywallActiveBlock()
                        PaywallLegalFooter()
                    } else {
                        PaywallAnalyticsHeader()
                        PaywallPlanArea(
                            state = state,
                            products = products,
                            showMonthly = showMonthly,
                            onShowMonthly = {
                                showMonthly = true
                                state.selectProduct(PremiumProductId.Month)
                            }
                        )
                        PaywallAnalyticsBento()
                        PaywallLegalFooter()
                    }

                    if (isDebug && isPremium.not()) {
                        TextButton(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = monetization::grantTestPremium
                        ) {
                            Text(text = stringResource(Res.string.paywall_test_premium))
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
        }

        if (hasPurchaseCta) {
            PaywallBottomBar(
                state = state,
                selectedProduct = selectedProduct,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun PaywallAnalyticsHeader() {
    Column {
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.paywall_analytics_eyebrow),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.paywall_analytics_headline),
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = 44.sp,
                lineHeight = 42.sp,
                letterSpacing = (-2).sp
            ),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = stringResource(Res.string.paywall_analytics_subhead),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PaywallAnalyticsBento() {
    Column(
        modifier = Modifier.padding(top = 18.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CalendarBentoCard()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DynamicsBentoCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            StatisticsBentoCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        DeckBentoCard()
    }
}

@Composable
private fun CalendarBentoCard() {
    BentoCard(modifier = Modifier.fillMaxWidth()) {
        BentoLabel(
            title = stringResource(Res.string.paywall_analytics_calendar_title),
            trailing = stringResource(Res.string.paywall_analytics_calendar_period)
        )
        Spacer(Modifier.height(12.dp))
        Heatmap()
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeatmapSwatch(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f))
                HeatmapSwatch(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                HeatmapSwatch(MaterialTheme.colorScheme.primary)
                Text(
                    text = stringResource(Res.string.paywall_analytics_calendar_legend),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = stringResource(Res.string.paywall_analytics_calendar_insight),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Heatmap() {
    val levels = List(56) { index ->
        ((index * 7 + index / 14 + 1) % 5)
    }
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        levels.chunked(14).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                row.forEach { level ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                color = heatLevelColor(level),
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun heatLevelColor(level: Int): Color = when (level) {
    1 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.27f)
    2 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.48f)
    3 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.72f)
    4 -> MaterialTheme.colorScheme.primary
    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
}

@Composable
private fun HeatmapSwatch(color: Color) {
    Box(
        modifier = Modifier
            .size(6.dp)
            .background(color = color, shape = RoundedCornerShape(2.dp))
    )
}

@Composable
private fun DynamicsBentoCard(modifier: Modifier) {
    BentoCard(modifier = modifier) {
        BentoLabel(
            title = stringResource(Res.string.paywall_analytics_dynamics_title),
            trailing = stringResource(Res.string.paywall_analytics_dynamics_change)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.paywall_analytics_dynamics_headline),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(Res.string.paywall_analytics_dynamics_subhead),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        PaywallSparkline(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )
    }
}

@Composable
private fun PaywallSparkline(modifier: Modifier) {
    val lineColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    Canvas(modifier = modifier) {
        val points = listOf(
            0.25f, 0.42f, 0.3f, 0.55f, 0.5f, 0.72f, 0.62f, 0.8f, 0.7f, 0.88f
        )
        val stepX = size.width / (points.size - 1)
        val gridStroke = 1.dp.toPx()

        listOf(0.25f, 0.5f, 0.75f).forEach { fraction ->
            drawLine(
                color = gridColor,
                start = Offset(0f, size.height * fraction),
                end = Offset(size.width, size.height * fraction),
                strokeWidth = gridStroke
            )
        }

        val path = Path()
        val fillPath = Path()
        points.forEachIndexed { index, value ->
            val x = stepX * index
            val y = size.height * (1f - value)
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }
        fillPath.lineTo(size.width, size.height)
        fillPath.lineTo(0f, size.height)
        fillPath.close()

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.3f), Color.Transparent)
            )
        )
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
private fun StatisticsBentoCard(modifier: Modifier) {
    BentoCard(modifier = modifier) {
        BentoLabel(
            title = stringResource(Res.string.paywall_analytics_statistics_title),
            trailing = "Premium"
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BentoStat(
                modifier = Modifier.weight(1f),
                value = "46",
                label = stringResource(Res.string.paywall_analytics_stat_clean_days)
            )
            BentoStat(
                modifier = Modifier.weight(1f),
                value = "1.8",
                label = stringResource(Res.string.paywall_analytics_stat_recovery)
            )
        }
        Spacer(Modifier.height(9.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            BentoStat(
                modifier = Modifier.weight(1f),
                value = "18",
                label = stringResource(Res.string.paywall_analytics_stat_best_streak)
            )
            BentoStat(
                modifier = Modifier.weight(1f),
                value = "16",
                label = stringResource(Res.string.paywall_analytics_stat_metrics)
            )
        }
    }
}

@Composable
private fun BentoStat(
    modifier: Modifier,
    value: String,
    label: String
) {
    Column(modifier = modifier) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 13.sp
        )
    }
}

@Composable
private fun DeckBentoCard() {
    BentoCard(modifier = Modifier.fillMaxWidth()) {
        BentoLabel(
            title = stringResource(Res.string.paywall_analytics_deck_title),
            trailing = stringResource(Res.string.paywall_analytics_deck_action)
        )
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ModuleChip(
                    label = stringResource(Res.string.paywall_analytics_module_calendar),
                    selected = true
                )
                ModuleChip(
                    label = stringResource(Res.string.paywall_analytics_module_dynamics),
                    selected = true
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ModuleChip(
                    label = stringResource(Res.string.paywall_analytics_module_statistics),
                    selected = false
                )
                ModuleChip(
                    label = stringResource(Res.string.paywall_analytics_module_weekly),
                    selected = true
                )
            }
        }
    }
}

@Composable
private fun ModuleChip(
    label: String,
    selected: Boolean
) {
    Surface(
        color = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        } else {
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f)
        },
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(50)
                    )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun BentoCard(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        )
    ) {
        Column(
            modifier = Modifier.padding(13.dp),
            content = content
        )
    }
}

@Composable
private fun BentoLabel(
    title: String,
    trailing: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.4.sp
        )
        Text(
            text = trailing,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PaywallPlanArea(
    state: PaywallScreenState,
    products: List<PremiumProduct>,
    showMonthly: Boolean,
    onShowMonthly: () -> Unit
) {
    Spacer(Modifier.height(22.dp))

    if (state.purchasesAvailable.not()) {
        UnavailableBlock(store = state.store)
        return
    }

    when (val productsState = state.productsState) {
        PaywallProductsState.Loading -> LoadingBlock()
        PaywallProductsState.Error -> ProductsErrorBlock(state)
        is PaywallProductsState.Loaded -> {
            if (products.isEmpty()) {
                ProductsErrorBlock(state)
            } else {
                LoadedPlans(
                    state = state,
                    products = products,
                    showMonthly = showMonthly,
                    onShowMonthly = onShowMonthly
                )
                if (productsState.isDebugStub) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = stringResource(Res.string.paywall_debug_stub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadedPlans(
    state: PaywallScreenState,
    products: List<PremiumProduct>,
    showMonthly: Boolean,
    onShowMonthly: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(Res.string.paywall_analytics_plans_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = stringResource(Res.string.paywall_analytics_plans_note),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Spacer(Modifier.height(12.dp))

    PrimaryPlanIds.forEach { productId ->
        val product = products.find { it.id == productId } ?: return@forEach
        PaywallPlanCard(
            productId = productId,
            product = product,
            selected = state.selectedProductId == productId,
            onSelect = { state.selectProduct(productId) }
        )
        Spacer(Modifier.height(8.dp))
    }

    val monthProduct = products.find { it.id == PremiumProductId.Month }
    if (monthProduct != null && (showMonthly || state.selectedProductId == PremiumProductId.Month)) {
        PaywallPlanCard(
            productId = PremiumProductId.Month,
            product = monthProduct,
            selected = state.selectedProductId == PremiumProductId.Month,
            onSelect = { state.selectProduct(PremiumProductId.Month) }
        )
        Spacer(Modifier.height(8.dp))
    } else if (monthProduct != null) {
        TextButton(
            onClick = onShowMonthly
        ) {
            Text(text = stringResource(Res.string.paywall_analytics_show_month))
        }
    }

    Text(
        modifier = Modifier.padding(top = 8.dp),
        text = stringResource(Res.string.paywall_analytics_included),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    ActionMessage(state.actionState)

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        enabled = state.actionState.isInProgress().not(),
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        onClick = state::restore
    ) {
        if (state.actionState is PaywallActionState.Restoring) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
        } else {
            Text(text = stringResource(Res.string.paywall_restore))
        }
    }
}

@Composable
private fun PaywallPlanCard(
    productId: PremiumProductId,
    product: PremiumProduct,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val trialDays = product.trialDays
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = stringResource(productId.titleResource()),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (trialDays != null) {
                        TrialPill(trialDays = trialDays)
                    } else if (productId == PremiumProductId.Year) {
                        BestValuePill()
                    }
                }
                product.monthlyPriceLabel?.let { monthly ->
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = stringResource(Res.string.paywall_per_month, monthly),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (productId == PremiumProductId.Lifetime) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = stringResource(Res.string.paywall_plan_once),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = product.priceLabel,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(productId.periodResource()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TrialPill(trialDays: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                modifier = Modifier.size(11.dp),
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(Res.string.paywall_analytics_trial_badge, trialDays),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun BestValuePill() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            text = stringResource(Res.string.paywall_plan_best),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun PaywallBottomBar(
    state: PaywallScreenState,
    selectedProduct: PremiumProduct,
    modifier: Modifier = Modifier
) {
    val trialDays = selectedProduct.trialDays
    val actionInProgress = state.actionState.isInProgress()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding(),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.97f),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = actionInProgress.not(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                ),
                onClick = state::purchase
            ) {
                if (state.actionState is PaywallActionState.Purchasing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.background
                    )
                } else {
                    Text(
                        text = when {
                            trialDays != null -> stringResource(
                                Res.string.paywall_cta_trial_days,
                                trialDays
                            )
                            state.selectedProductId == PremiumProductId.Lifetime -> stringResource(
                                Res.string.paywall_analytics_cta_lifetime
                            )
                            state.selectedProductId == PremiumProductId.Month -> stringResource(
                                Res.string.paywall_analytics_cta_month
                            )
                            else -> stringResource(Res.string.paywall_purchase)
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = when {
                    trialDays != null -> stringResource(
                        Res.string.paywall_analytics_trial_note,
                        priceText(state.selectedProductId, selectedProduct)
                    )
                    state.selectedProductId == PremiumProductId.Lifetime -> stringResource(
                        Res.string.paywall_lifetime_note
                    )
                    else -> stringResource(Res.string.paywall_renewal_note)
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PaywallLegalFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            TextButton(
                contentPadding = PaddingValues(horizontal = 4.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { UrlOpener.openUrl(PRIVACY_POLICY_URL) }
            ) {
                Text(
                    text = stringResource(Res.string.paywall_privacy),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            TextButton(
                contentPadding = PaddingValues(horizontal = 4.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = { UrlOpener.openUrl(TERMS_OF_USE_URL) }
            ) {
                Text(
                    text = stringResource(Res.string.paywall_terms),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Text(
            text = stringResource(Res.string.paywall_manage_note),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoadingBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(Res.string.paywall_loading))
        }
    }
}

@Composable
private fun ProductsErrorBlock(state: PaywallScreenState) {
    Column {
        Text(text = stringResource(Res.string.paywall_products_error))
        TextButton(onClick = state::loadProducts) {
            Text(text = stringResource(Res.string.paywall_retry))
        }
    }
}

@Composable
private fun PaywallActiveBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.paywall_premium_active_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.paywall_premium_active_message),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionMessage(actionState: PaywallActionState) {
    if (actionState is PaywallActionState.Message) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            text = stringResource(actionState.message.messageResource()),
            color = if (actionState.message.isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UnavailableBlock(store: PurchaseStore) {
    val title = when (store) {
        PurchaseStore.GooglePlay -> Res.string.paywall_googleplay_title
        PurchaseStore.Ios -> Res.string.paywall_ios_title
        PurchaseStore.RuStore -> Res.string.paywall_unavailable_title
    }
    val message = when (store) {
        PurchaseStore.GooglePlay -> Res.string.paywall_googleplay_message
        PurchaseStore.Ios -> Res.string.paywall_ios_message
        PurchaseStore.RuStore -> Res.string.paywall_unavailable_message
    }

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun priceText(productId: PremiumProductId, product: PremiumProduct?): String {
    val price = product?.priceLabel ?: "—"
    return when (productId) {
        PremiumProductId.Year -> stringResource(Res.string.paywall_price_per_year, price)
        PremiumProductId.Month -> stringResource(Res.string.paywall_price_per_month, price)
        PremiumProductId.Lifetime -> price
    }
}

private fun PaywallActionState.isInProgress() =
    this is PaywallActionState.Purchasing || this is PaywallActionState.Restoring

private fun PremiumProductId.titleResource(): StringResource = when (this) {
    PremiumProductId.Lifetime -> Res.string.paywall_offer_lifetime
    PremiumProductId.Month -> Res.string.paywall_offer_month
    PremiumProductId.Year -> Res.string.paywall_offer_year
}

private fun PremiumProductId.periodResource(): StringResource = when (this) {
    PremiumProductId.Lifetime -> Res.string.paywall_analytics_once
    PremiumProductId.Month -> Res.string.paywall_analytics_per_month
    PremiumProductId.Year -> Res.string.paywall_analytics_per_year
}

private fun PaywallMessage.messageResource(): StringResource = when (this) {
    PaywallMessage.PurchaseCancelled -> Res.string.paywall_purchase_cancelled
    PaywallMessage.PurchaseFailed -> Res.string.paywall_purchase_error
    PaywallMessage.RestoreFailed -> Res.string.paywall_restore_error
    PaywallMessage.NothingFound -> Res.string.paywall_restore_nothing
}
