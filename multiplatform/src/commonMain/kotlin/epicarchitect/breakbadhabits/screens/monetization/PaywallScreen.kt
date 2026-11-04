package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import epicarchitect.breakbadhabits.monetization.RestoreFailureReason
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.app_name
import epicarchitect.breakbadhabits.resources.paywall_analytics_per_year
import epicarchitect.breakbadhabits.resources.paywall_app_icon
import epicarchitect.breakbadhabits.resources.paywall_close
import epicarchitect.breakbadhabits.resources.paywall_continue
import epicarchitect.breakbadhabits.resources.paywall_discount
import epicarchitect.breakbadhabits.resources.paywall_feature_histogram_subtitle
import epicarchitect.breakbadhabits.resources.paywall_feature_histogram_title
import epicarchitect.breakbadhabits.resources.paywall_feature_risk_subtitle
import epicarchitect.breakbadhabits.resources.paywall_feature_risk_title
import epicarchitect.breakbadhabits.resources.paywall_feature_unlimited_subtitle
import epicarchitect.breakbadhabits.resources.paywall_feature_unlimited_title
import epicarchitect.breakbadhabits.resources.paywall_feature_widget_subtitle
import epicarchitect.breakbadhabits.resources.paywall_feature_widget_title
import epicarchitect.breakbadhabits.resources.paywall_googleplay_message
import epicarchitect.breakbadhabits.resources.paywall_googleplay_title
import epicarchitect.breakbadhabits.resources.paywall_intro_offer
import epicarchitect.breakbadhabits.resources.paywall_ios_message
import epicarchitect.breakbadhabits.resources.paywall_ios_title
import epicarchitect.breakbadhabits.resources.paywall_offer_lifetime
import epicarchitect.breakbadhabits.resources.paywall_offer_month
import epicarchitect.breakbadhabits.resources.paywall_offer_year
import epicarchitect.breakbadhabits.resources.paywall_per_month
import epicarchitect.breakbadhabits.resources.paywall_plan_savings
import epicarchitect.breakbadhabits.resources.paywall_plan_unit_month
import epicarchitect.breakbadhabits.resources.paywall_plan_unit_year
import epicarchitect.breakbadhabits.resources.paywall_products_error
import epicarchitect.breakbadhabits.resources.paywall_restore_unknown_error
import epicarchitect.breakbadhabits.resources.paywall_restore_store_error
import epicarchitect.breakbadhabits.resources.paywall_restore_no_purchases
import epicarchitect.breakbadhabits.resources.paywall_restore_failed_title
import epicarchitect.breakbadhabits.resources.paywall_restore_success_message
import epicarchitect.breakbadhabits.resources.paywall_restore_success_title
import epicarchitect.breakbadhabits.resources.common_ok
import epicarchitect.breakbadhabits.resources.paywall_privacy
import epicarchitect.breakbadhabits.resources.paywall_terms
import epicarchitect.breakbadhabits.resources.paywall_restore
import epicarchitect.breakbadhabits.resources.paywall_promotion_until
import epicarchitect.breakbadhabits.resources.paywall_purchase_cancelled
import epicarchitect.breakbadhabits.resources.paywall_purchase_error
import epicarchitect.breakbadhabits.resources.paywall_retry
import epicarchitect.breakbadhabits.resources.paywall_tariff_lifetime_note
import epicarchitect.breakbadhabits.resources.paywall_tariff_month_note
import epicarchitect.breakbadhabits.resources.paywall_trial_badge
import epicarchitect.breakbadhabits.resources.paywall_unavailable_message
import epicarchitect.breakbadhabits.resources.paywall_unavailable_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AdaptiveContentWidth
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.PremiumShimmerLabel
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.TERMS_OF_USE_URL
import epicarchitect.breakbadhabits.url.openUrl
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

private val HeatLevels = listOf(
    0, 1, 0, 2, 0, 0, 1,
    1, 0, 2, 3, 1, 0, 0,
    1, 2, 0, 3, 4, 2, 0,
    1, 0, 2, 1, 0, 1, 0
)

private val BarHeights = listOf(0.20f, 0.38f, 0.27f, 0.56f, 0.92f, 0.48f, 0.35f)
private val PaywallFeatureRowMinHeight = 74.dp

@Composable
fun PaywallScreen(source: String = "unknown") {
    val monetization = Monetization
    val state = rememberPaywallScreenState(source)
    val premiumCache by monetization.premiumCache.collectAsState()
    val isPremium = premiumCache?.expiresAt?.let { it > Clock.System.now() } == true
    val navBackStack = LocalNavBackStack.current

    PaywallScreenContent(
        state = state,
        isPremium = isPremium,
        onClose = { navBackStack.removeLastOrNull() }
    )
}

@Composable
fun PaywallScreenPreview(state: PaywallScreenState) {
    PaywallScreenContent(
        state = state,
        isPremium = false,
        onClose = {}
    )
}

@Composable
private fun PaywallScreenContent(
    state: PaywallScreenState,
    isPremium: Boolean,
    onClose: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val products = (state.productsState as? PaywallProductsState.Loaded)?.products.orEmpty()
    val selectedProduct = products.find { it.id == state.selectedProductId }
    val showCta = state.purchasesAvailable && selectedProduct != null
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val inspectionMode = LocalInspectionMode.current
    var contentVisible by remember { mutableStateOf(inspectionMode) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (contentVisible || inspectionMode) 1f else 0f,
        animationSpec = tween(
            durationMillis = 280,
            easing = FastOutSlowInEasing
        ),
        label = "paywallContentAlpha"
    )

    LaunchedEffect(Unit) {
        contentVisible = true
    }

    LaunchedEffect(isPremium, state.restoreFlowActive) {
        if (isPremium && !state.restoreFlowActive) onClose()
    }

    if (isPremium && !state.restoreFlowActive) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scheme.background)
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = scheme.background)
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            scheme.primary.copy(alpha = 0.12f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2f, -size.height * 0.08f),
                        radius = size.width * 1.20f
                    )
                )
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            scheme.primary.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        center = Offset(size.width / 2f, size.height * 1.08f),
                        radius = size.width * 0.95f
                    )
                )
            }
    ) {
        AdaptiveContent(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            width = AdaptiveContentWidth.Reading
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .graphicsLayer { alpha = contentAlpha }
            ) {
                PaywallBrandHeader(onClose = onClose)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    PaywallFeatureList(
                        modifier = Modifier.fillMaxWidth()
                    )
                    PaywallPlans(
                        state = state,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                PaywallCta(
                    state = state,
                    selectedProduct = selectedProduct,
                    showCta = showCta
                )
                PaywallLegalFooter(state = state)
                Spacer(Modifier.height(20.dp + navigationBarBottom))
            }
        }
        state.restoreOutcome?.let { outcome ->
            PaywallRestoreResultDialog(
                outcome = outcome,
                onDismiss = state::dismissRestoreOutcome
            )
        }
    }
}

@Composable
private fun PaywallRestoreResultDialog(
    outcome: RestoreOutcome,
    onDismiss: () -> Unit
) {
    val title = when (outcome) {
        RestoreOutcome.Success -> stringResource(Res.string.paywall_restore_success_title)
        is RestoreOutcome.Failed -> stringResource(Res.string.paywall_restore_failed_title)
    }
    val message = when (outcome) {
        RestoreOutcome.Success -> stringResource(Res.string.paywall_restore_success_message)
        is RestoreOutcome.Failed -> when (outcome.reason) {
            RestoreFailureReason.NoPurchases ->
                stringResource(Res.string.paywall_restore_no_purchases)

            RestoreFailureReason.StoreError -> outcome.details
                ?.takeIf { it.isNotBlank() }
                ?.let { stringResource(Res.string.paywall_restore_store_error, it) }
                ?: stringResource(Res.string.paywall_restore_unknown_error)

            RestoreFailureReason.Unknown ->
                stringResource(Res.string.paywall_restore_unknown_error)
        }
    }
    Dialog(onDismiss = onDismiss) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onDismiss) {
                    Text(stringResource(Res.string.common_ok))
                }
            }
        }
    }
}

@Composable
private fun PaywallCloseButton(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(Res.string.paywall_close),
            tint = scheme.onBackground
        )
    }
}

@Composable
private fun PaywallBrandHeader(onClose: () -> Unit) {
    val scheme = MaterialTheme.colorScheme
    val iconShape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .size(44.dp)
                .clip(iconShape)
                .background(scheme.background)
                .border(
                    width = 1.dp,
                    color = scheme.onSurface.copy(alpha = 0.09f),
                    shape = iconShape
                ),
            painter = painterResource(Res.drawable.paywall_app_icon),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BasicText(
                text = stringResource(Res.string.app_name),
                style = TextStyle(
                    color = scheme.onBackground,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = (-0.1).sp
                ),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 9.sp,
                    maxFontSize = 12.sp,
                    stepSize = 0.5.sp
                )
            )
            PremiumShimmerLabel()
        }
        PaywallCloseButton(onClose = onClose)
    }
}

@Composable
private fun PaywallFeatureList(
    modifier: Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val features = listOf(
        PaywallFeatureItem(
            title = Res.string.paywall_feature_unlimited_title,
            subtitle = Res.string.paywall_feature_unlimited_subtitle,
            visual = { UnlimitedArt() }
        ),
        PaywallFeatureItem(
            title = Res.string.paywall_feature_risk_title,
            subtitle = Res.string.paywall_feature_risk_subtitle,
            visual = { RiskHeatmap() }
        ),
        PaywallFeatureItem(
            title = Res.string.paywall_feature_histogram_title,
            subtitle = Res.string.paywall_feature_histogram_subtitle,
            visual = { AbstinenceBars() }
        ),
        PaywallFeatureItem(
            title = Res.string.paywall_feature_widget_title,
            subtitle = Res.string.paywall_feature_widget_subtitle,
            visual = { WidgetMini() }
        )
    )

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        features.forEachIndexed { index, feature ->
            PaywallFeatureRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = PaywallFeatureRowMinHeight),
                title = stringResource(feature.title),
                subtitle = stringResource(feature.subtitle),
                visual = feature.visual
            )
            if (index != features.lastIndex) {
                HorizontalDivider(
                    color = scheme.outlineVariant.copy(alpha = 0.50f),
                    thickness = 1.dp
                )
            }
        }
    }
}

private data class PaywallFeatureItem(
    val title: StringResource,
    val subtitle: StringResource,
    val visual: @Composable () -> Unit
)

@Composable
private fun PaywallFeatureRow(
    modifier: Modifier,
    title: String,
    subtitle: String,
    visual: @Composable () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = scheme.onBackground,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.1).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = scheme.onSurface.copy(alpha = 0.60f),
                fontSize = 13.sp,
                lineHeight = 17.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(14.dp))
        Box(
            modifier = Modifier
                .width(72.dp)
                .height(44.dp),
            contentAlignment = Alignment.Center
        ) {
            visual()
        }
    }
}

@Composable
private fun UnlimitedArt() {
    val scheme = MaterialTheme.colorScheme
    Canvas(
        modifier = Modifier
            .width(72.dp)
            .height(40.dp)
    ) {
        val scale = minOf(
            size.width / 80f,
            size.height / 48f
        )
        val offsetX = (size.width - 80f * scale) / 2f
        val offsetY = (size.height - 48f * scale) / 2f
        fun x(value: Float) = value * scale + offsetX
        fun y(value: Float) = value * scale + offsetY

        val leftBody = Path().apply {
            moveTo(x(40f), y(37f))
            lineTo(x(16f), y(37f))
            lineTo(x(6f), y(13f))
            lineTo(x(18f), y(23f))
            lineTo(x(25f), y(10f))
            lineTo(x(31f), y(23f))
            lineTo(x(40f), y(4f))
            close()
        }
        val rightBody = Path().apply {
            moveTo(x(40f), y(37f))
            lineTo(x(64f), y(37f))
            lineTo(x(74f), y(13f))
            lineTo(x(62f), y(23f))
            lineTo(x(55f), y(10f))
            lineTo(x(49f), y(23f))
            lineTo(x(40f), y(4f))
            close()
        }
        val darkenTarget = if (scheme.background.luminance() < 0.5f) {
            scheme.background
        } else {
            scheme.onBackground
        }
        val lightBody = lerp(scheme.primary, darkenTarget, 0.12f)
        val darkBody = lerp(scheme.primary, darkenTarget, 0.38f)
        drawPath(path = leftBody, color = lightBody)
        drawPath(path = rightBody, color = darkBody)
        val tipCenter = lerp(lightBody, darkBody, 0.5f)
        listOf(
            Triple(6f, 13f, lightBody),
            Triple(25f, 10f, lightBody),
            Triple(40f, 4f, tipCenter),
            Triple(55f, 10f, darkBody),
            Triple(74f, 13f, darkBody)
        ).forEach { (tipX, tipY, tipColor) ->
            drawCircle(
                color = tipColor,
                radius = if (tipX == 40f) 2f * scale else 1.8f * scale,
                center = Offset(x(tipX), y(tipY))
            )
        }

        drawRect(
            color = lightBody.copy(alpha = 0.84f),
            topLeft = Offset(x(16f), y(40f)),
            size = Size(24f * scale, 5f * scale)
        )
        drawRect(
            color = darkBody.copy(alpha = 0.84f),
            topLeft = Offset(x(40f), y(40f)),
            size = Size(24f * scale, 5f * scale)
        )

        val gemLeft = Path().apply {
            moveTo(x(40f), y(17f))
            lineTo(x(33f), y(23f))
            lineTo(x(36f), y(33f))
            lineTo(x(40f), y(37f))
            close()
        }
        val gemRight = Path().apply {
            moveTo(x(40f), y(17f))
            lineTo(x(47f), y(23f))
            lineTo(x(44f), y(33f))
            lineTo(x(40f), y(37f))
            close()
        }
        drawPath(
            path = gemLeft,
            color = lerp(scheme.primary, scheme.onPrimary, 0.42f)
        )
        drawPath(
            path = gemRight,
            color = lerp(scheme.primary, scheme.onPrimary, 0.16f)
        )
    }
}

@Composable
private fun RiskHeatmap() {
    val scheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .width(72.dp)
            .height(44.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        HeatLevels.chunked(7).forEach { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                row.forEach { level ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                color = heatLevelColor(level, scheme),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }
    }
}

private fun heatLevelColor(level: Int, scheme: ColorScheme): Color = when (level) {
    1 -> scheme.primary.copy(alpha = 0.28f)
    2 -> scheme.primary.copy(alpha = 0.55f)
    3 -> scheme.primary
    4 -> scheme.primary
    else -> scheme.onSurface.copy(alpha = 0.10f)
}

@Composable
private fun AbstinenceBars() {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .width(72.dp)
            .height(44.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        BarHeights.forEachIndexed { index, heightFraction ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height((46f * heightFraction).dp)
                    .background(
                        color = when (index) {
                            4 -> scheme.primary
                            5 -> scheme.primary.copy(alpha = 0.60f)
                            else -> scheme.onSurface.copy(alpha = 0.22f)
                        },
                        shape = RoundedCornerShape(
                            topStart = 3.dp,
                            topEnd = 3.dp,
                            bottomStart = 1.dp,
                            bottomEnd = 1.dp
                        )
                    )
            )
        }
    }
}

@Composable
private fun WidgetMini() {
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(11.dp)
    Column(
        modifier = Modifier
            .width(72.dp)
            .height(44.dp)
            .clip(shape)
            .background(scheme.surfaceContainerLow)
            .padding(7.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        WidgetMiniRow()
        WidgetMiniRow()
    }
}

@Composable
private fun WidgetMiniRow() {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = scheme.primary.copy(alpha = 0.80f),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(3.dp)
                .background(
                    color = scheme.onSurface.copy(alpha = 0.09f),
                    shape = RoundedCornerShape(9.dp)
                )
        )
    }
}

@Composable
private fun PaywallPlans(
    state: PaywallScreenState,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(top = 8.dp)
    ) {
        if (state.purchasesAvailable.not()) {
            PaywallStoreNotice(store = state.store)
        } else if (LocalInspectionMode.current) {
            PaywallPlansContent(
                state = state,
                productsState = state.productsState
            )
        } else {
            AnimatedContent(
                targetState = state.productsState,
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 240,
                            easing = FastOutSlowInEasing
                        )
                    ) togetherWith fadeOut(
                        animationSpec = tween(durationMillis = 140)
                    )
                },
                contentAlignment = Alignment.TopStart,
                label = "paywallPlans"
            ) { productsState ->
                PaywallPlansContent(
                    state = state,
                    productsState = productsState
                )
            }
        }
    }
}

@Composable
private fun PaywallPlansContent(
    state: PaywallScreenState,
    productsState: PaywallProductsState
) {
    when (productsState) {
        PaywallProductsState.Loading -> PaywallPlansSkeleton()
        PaywallProductsState.Error -> PaywallProductsError(state = state)
        is PaywallProductsState.Loaded -> {
            if (productsState.products.isEmpty()) {
                PaywallProductsError(state = state)
            } else {
                PaywallTariffs(
                    state = state,
                    products = productsState.products
                )
            }
        }
    }
}

@Composable
private fun PaywallTariffs(
    state: PaywallScreenState,
    products: List<PremiumProduct>
) {
    val monthly = products.find { it.id == PremiumProductId.Month }
    val year = products.find { it.id == PremiumProductId.Year }
    val lifetime = products.find { it.id == PremiumProductId.Lifetime }
    val now = Clock.System.now()
    val discount = discountPercent(monthly = monthly, year = year, now = now)
    val savingsAmount = savingsAmountLabel(monthly = monthly, year = year, now = now)
    val yearNote = year?.monthlyPriceLabel?.let { monthlyLabel ->
        stringResource(Res.string.paywall_per_month, monthlyLabel)
    } ?: stringResource(Res.string.paywall_analytics_per_year)
    val monthNote = monthly?.trialDays?.let { trialDays ->
        pluralStringResource(Res.plurals.paywall_trial_badge, trialDays, trialDays)
    } ?: stringResource(Res.string.paywall_tariff_month_note)

    val tariffs = listOf(
        PaywallTariffOption(
            id = PremiumProductId.Month,
            product = monthly,
            note = monthNote
        ),
        PaywallTariffOption(
            id = PremiumProductId.Year,
            product = year,
            note = yearNote,
            discountPercent = discount,
            savingsAmount = savingsAmount
        ),
        PaywallTariffOption(
            id = PremiumProductId.Lifetime,
            product = lifetime,
            note = stringResource(Res.string.paywall_tariff_lifetime_note)
        )
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tariffs.forEach { tariff ->
            PaywallTariffRow(
                tariff = tariff,
                selected = state.selectedProductId == tariff.id,
                onSelect = { state.selectProduct(tariff.id) }
            )
        }
    }
}

private data class PaywallTariffOption(
    val id: PremiumProductId,
    val product: PremiumProduct?,
    val note: String,
    val discountPercent: Int? = null,
    val savingsAmount: String? = null
)

@Composable
private fun PaywallTariffRow(
    tariff: PaywallTariffOption,
    selected: Boolean,
    onSelect: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)
    val softAccent = lerp(scheme.primary, scheme.onPrimary, 0.45f)
    val fillBrush = if (selected) {
        Brush.linearGradient(
            colors = listOf(
                scheme.primary.copy(alpha = 0.24f),
                scheme.primary.copy(alpha = 0.08f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                scheme.onSurface.copy(alpha = 0.045f),
                scheme.onSurface.copy(alpha = 0.018f)
            )
        )
    }
    val unit = when (tariff.id) {
        PremiumProductId.Month -> stringResource(Res.string.paywall_plan_unit_month)
        PremiumProductId.Year -> stringResource(Res.string.paywall_plan_unit_year)
        PremiumProductId.Lifetime -> null
    }
    val now = Clock.System.now()
    val promotion = tariff.product
        ?.promotion
        ?.takeIf { it.endDate == null || it.endDate > now }
    val badgeDiscountPercent = promotion?.percent ?: tariff.discountPercent
    val introOffer = tariff.product?.introOffer
    val introNote = introOffer?.let { offer ->
        stringResource(
            Res.string.paywall_intro_offer,
            offer.priceLabel,
            offer.nextPriceLabel + unit.orEmpty()
        )
    }
    val note = if (tariff.product?.trialDays == null && introNote != null) {
        introNote
    } else {
        tariff.note
    }
    val promotionUntil = promotion?.endDate?.let { endDate ->
        endDate
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .formatted()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        onClick = onSelect,
        enabled = tariff.product != null,
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                scheme.primary.copy(alpha = 0.72f)
            } else {
                scheme.outlineVariant.copy(alpha = 0.50f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(fillBrush, shape)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 1.5.dp,
                        color = if (selected) {
                            scheme.primary
                        } else {
                            scheme.onSurface.copy(alpha = 0.28f)
                        },
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(scheme.primary, CircleShape)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f, fill = false),
                        text = stringResource(tariff.id.titleResource()),
                        color = scheme.onBackground,
                        fontSize = 15.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.1).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    badgeDiscountPercent?.let { discountPercent ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(scheme.primary.copy(alpha = 0.17f))
                                .border(
                                    width = 1.dp,
                                    color = scheme.primary.copy(alpha = 0.38f),
                                    shape = RoundedCornerShape(50)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = stringResource(
                                    Res.string.paywall_discount,
                                    discountPercent
                                ) + "%",
                                color = softAccent,
                                fontSize = 10.sp,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Text(
                    text = note,
                    color = if (tariff.id == PremiumProductId.Year) {
                        scheme.onBackground
                    } else {
                        scheme.onSurface.copy(alpha = 0.60f)
                    },
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = if (tariff.id == PremiumProductId.Year) {
                        FontWeight.Medium
                    } else {
                        FontWeight.Normal
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    promotion?.let { promotion ->
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = promotion.basePriceLabel,
                            color = scheme.onSurface.copy(alpha = 0.45f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            textDecoration = TextDecoration.LineThrough,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = tariff.product?.priceLabel ?: "—",
                        color = scheme.onBackground,
                        fontSize = 20.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.4).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    unit?.let { unitText ->
                        Text(
                            modifier = Modifier.alignByBaseline(),
                            text = unitText,
                            color = scheme.onSurface.copy(alpha = 0.60f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            maxLines = 1
                        )
                    }
                }
                if (promotion == null) {
                    tariff.savingsAmount?.let { amount ->
                        Text(
                            text = stringResource(Res.string.paywall_plan_savings, amount),
                            color = softAccent,
                            fontSize = 11.sp,
                            lineHeight = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                promotionUntil?.let { until ->
                    Text(
                        text = stringResource(Res.string.paywall_promotion_until, until),
                        color = scheme.onSurface.copy(alpha = 0.45f),
                        fontSize = 10.sp,
                        lineHeight = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PaywallPlansSkeleton() {
    val progress = rememberPaywallShimmerProgress()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            PaywallTariffSkeleton(
                progress = progress,
                withSavings = index == 1
            )
        }
    }
}

@Composable
private fun PaywallTariffSkeleton(
    progress: Float,
    withSavings: Boolean
) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(
            width = 1.dp,
            color = scheme.outlineVariant.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SkeletonBlock(
                progress = progress,
                modifier = Modifier.size(20.dp),
                shape = CircleShape
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SkeletonBlock(
                        progress = progress,
                        modifier = Modifier
                            .width(72.dp)
                            .height(14.dp)
                    )
                    if (withSavings) {
                        SkeletonBlock(
                            progress = progress,
                            modifier = Modifier
                                .width(38.dp)
                                .height(18.dp),
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
                SkeletonBlock(
                    progress = progress,
                    modifier = Modifier
                        .width(92.dp)
                        .height(9.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                SkeletonBlock(
                    progress = progress,
                    modifier = Modifier
                        .width(78.dp)
                        .height(20.dp)
                )
                SkeletonBlock(
                    progress = progress,
                    modifier = Modifier
                        .width(if (withSavings) 78.dp else 40.dp)
                        .height(9.dp)
                )
            }
        }
    }
}

@Composable
private fun PaywallCtaSkeleton() {
    SkeletonBlock(
        progress = rememberPaywallShimmerProgress(),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun rememberPaywallShimmerProgress(): Float {
    val transition = rememberInfiniteTransition(label = "paywallSkeleton")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1300,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "paywallSkeletonProgress"
    )
    return progress
}

@Composable
private fun SkeletonBlock(
    progress: Float,
    modifier: Modifier,
    shape: Shape = RoundedCornerShape(6.dp)
) {
    val scheme = MaterialTheme.colorScheme
    val base = scheme.onSurface.copy(alpha = 0.055f)
    val highlight = scheme.onSurface.copy(alpha = 0.12f)
    Box(
        modifier = modifier
            .clip(shape)
            .drawWithCache {
                val bandWidth = size.width * 0.9f
                val startX = -bandWidth + progress * (size.width + bandWidth * 2f)
                val brush = Brush.linearGradient(
                    colors = listOf(base, highlight, base),
                    start = Offset(startX, 0f),
                    end = Offset(startX + bandWidth, 0f)
                )
                onDrawBehind {
                    drawRect(brush)
                }
            }
    )
}

@Composable
private fun PaywallProductsError(state: PaywallScreenState) {
    val scheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.paywall_products_error),
            color = scheme.onSurface.copy(alpha = 0.60f),
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
        TextButton(onClick = state::loadProducts) {
            Text(
                text = stringResource(Res.string.paywall_retry),
                color = scheme.primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PaywallStoreNotice(store: PurchaseStore) {
    val scheme = MaterialTheme.colorScheme
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
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp),
        shape = RoundedCornerShape(15.dp),
        color = scheme.onSurface.copy(alpha = 0.04f),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.50f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(title),
                color = scheme.onBackground,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = stringResource(message),
                color = scheme.onSurface.copy(alpha = 0.60f),
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PaywallCta(
    state: PaywallScreenState,
    selectedProduct: PremiumProduct?,
    showCta: Boolean
) {
    val actionState = state.actionState
    val actionInProgress = actionState.isInProgress()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        if (actionState is PaywallActionState.Message) {
            PaywallActionMessage(actionState = actionState)
            Spacer(Modifier.height(6.dp))
        }

        if (state.purchasesAvailable) {
            val product = selectedProduct
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (state.productsState is PaywallProductsState.Loading) {
                    PaywallCtaSkeleton()
                } else if (showCta && product != null) {
                    PaywallContinueButton(
                        label = stringResource(Res.string.paywall_continue),
                        enabled = actionInProgress.not(),
                        purchasing = actionState is PaywallActionState.Purchasing,
                        onClick = state::purchase
                    )
                }
            }
        }
    }
}

@Composable
private fun PaywallLegalFooter(state: PaywallScreenState) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        if (state.purchasesAvailable) {
            PaywallFooterLink(
                text = stringResource(Res.string.paywall_restore),
                onClick = state::restorePurchases
            )
            PaywallFooterSeparator()
        }
        PaywallFooterLink(
            text = stringResource(Res.string.paywall_terms),
            onClick = { openUrl(TERMS_OF_USE_URL) }
        )
        PaywallFooterSeparator()
        PaywallFooterLink(
            text = stringResource(Res.string.paywall_privacy),
            onClick = { openUrl(PRIVACY_POLICY_URL) }
        )
    }
}

@Composable
private fun PaywallFooterLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        text = text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
        fontSize = 11.sp,
        lineHeight = 14.sp,
        maxLines = 1
    )
}

@Composable
private fun PaywallFooterSeparator() {
    Text(
        modifier = Modifier.padding(horizontal = 2.dp),
        text = "·",
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
}

@Composable
private fun PaywallContinueButton(
    label: String,
    enabled: Boolean,
    purchasing: Boolean,
    onClick: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = scheme.primary,
            contentColor = scheme.onPrimary,
            disabledContainerColor = scheme.primary.copy(alpha = 0.55f),
            disabledContentColor = scheme.onPrimary.copy(alpha = 0.80f)
        ),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        if (purchasing) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = scheme.onPrimary,
                strokeWidth = 1.5.dp
            )
        } else {
            Text(
                text = label,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.12).sp
            )
        }
    }
}

@Composable
private fun PaywallActionMessage(actionState: PaywallActionState.Message) {
    val scheme = MaterialTheme.colorScheme
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(actionState.message.messageResource()),
        color = if (actionState.message.isError) {
            scheme.error
        } else {
            scheme.onSurface.copy(alpha = 0.60f)
        },
        fontSize = 12.sp,
        lineHeight = 16.sp,
        textAlign = TextAlign.Center
    )
}

private fun savingsAmountLabel(
    monthly: PremiumProduct?,
    year: PremiumProduct?,
    now: Instant
): String? {
    val monthlyValue = monthly?.regularPriceMinorUnits(now) ?: return null
    val yearValue = year?.priceMinorUnits ?: return null
    val savings = monthlyValue * 12 - yearValue
    if (savings < 100) return null

    val amount = (savings / 100).formatted()
    val currency = year.priceLabel
        .filterNot { it.isDigit() || it.isWhitespace() || it == ',' || it == '.' }
        .trim()
    return if (currency.isEmpty()) amount else "$amount $currency"
}

private fun PremiumProduct.regularPriceMinorUnits(now: Instant): Int? =
    promotion
        ?.takeIf { it.endDate == null || it.endDate > now }
        ?.basePriceMinorUnits
        ?: priceMinorUnits

private fun discountPercent(
    monthly: PremiumProduct?,
    year: PremiumProduct?,
    now: Instant
): Int? {
    val monthlyValue = monthly?.regularPriceMinorUnits(now) ?: return null
    val yearValue = year?.priceMinorUnits ?: return null
    if (monthlyValue <= 0 || yearValue <= 0) return null

    val fullYearValue = monthlyValue.toDouble() * 12.0
    if (yearValue >= fullYearValue) return null

    val percent = ((1.0 - yearValue / fullYearValue) * 100.0).roundToInt()
    return percent.takeIf { it in 1..99 }
}

private fun PaywallActionState.isInProgress() =
    this is PaywallActionState.Purchasing

private fun PremiumProductId.titleResource(): StringResource = when (this) {
    PremiumProductId.Lifetime -> Res.string.paywall_offer_lifetime
    PremiumProductId.Month -> Res.string.paywall_offer_month
    PremiumProductId.Year -> Res.string.paywall_offer_year
}

private fun PaywallMessage.messageResource(): StringResource = when (this) {
    PaywallMessage.PurchaseCancelled -> Res.string.paywall_purchase_cancelled
    PaywallMessage.PurchaseFailed -> Res.string.paywall_purchase_error
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall trial")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall trial RU", locale = "ru")
@Composable
private fun PaywallTrialPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewTrialProducts),
        selectedProduct = PremiumProductId.Month,
        products = previewTrialProducts
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall promo")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall promo RU", locale = "ru")
@Composable
private fun PaywallPromoPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewPromoProducts),
        selectedProduct = PremiumProductId.Month,
        products = previewPromoProducts
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall intro")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall intro RU", locale = "ru")
@Composable
private fun PaywallIntroPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewIntroProducts),
        selectedProduct = PremiumProductId.Month,
        products = previewIntroProducts
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall loading")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall loading RU", locale = "ru")
@Composable
private fun PaywallLoadingPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loading,
        selectedProduct = null,
        products = emptyList()
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall error")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall error RU", locale = "ru")
@Composable
private fun PaywallErrorPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Error,
        selectedProduct = null,
        products = emptyList()
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall google play")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall google play RU", locale = "ru")
@Composable
private fun PaywallGooglePlayPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewTrialProducts),
        selectedProduct = PremiumProductId.Year,
        products = previewTrialProducts,
        purchasesAvailable = false
    )
}

@Composable
private fun PreviewPaywall(
    initialState: PaywallProductsState,
    selectedProduct: PremiumProductId?,
    products: List<PremiumProduct>,
    purchasesAvailable: Boolean = true
) {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(initialState, products, selectedProduct, purchasesAvailable) {
        PaywallScreenState(
            monetization = PreviewMonetization(
                products = products,
                purchasesAvailable = purchasesAvailable
            ),
            coroutineScope = coroutineScope,
            source = "preview",
            initialProductsState = initialState
        )
    }

    LaunchedEffect(state, selectedProduct) {
        selectedProduct?.let(state::selectProduct)
    }

    PreviewTheme {
        PaywallScreenPreview(state)
    }
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall year promo")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall year promo RU", locale = "ru")
@Composable
private fun PaywallYearPromoPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewYearPromoProducts),
        selectedProduct = PremiumProductId.Year,
        products = previewYearPromoProducts
    )
}

@Preview(widthDp = 412, heightDp = 915, name = "Paywall lifetime promo")
@Preview(widthDp = 412, heightDp = 915, name = "Paywall lifetime promo RU", locale = "ru")
@Composable
private fun PaywallLifetimePromoPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewLifetimePromoProducts),
        selectedProduct = PremiumProductId.Lifetime,
        products = previewLifetimePromoProducts
    )
}

@Preview(widthDp = 360, heightDp = 420, name = "Paywall plurals EN", locale = "en")
@Preview(widthDp = 360, heightDp = 420, name = "Paywall plurals RU", locale = "ru")
@Composable
private fun PaywallTrialPluralPreview() {
    PreviewTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(1, 2, 3, 4, 5, 11, 21, 22, 25).forEach { count ->
                Text(
                    text = pluralStringResource(
                        Res.plurals.paywall_trial_badge,
                        count,
                        count
                    )
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 260, name = "Paywall restore success")
@Preview(widthDp = 360, heightDp = 260, name = "Paywall restore success RU", locale = "ru")
@Composable
private fun PaywallRestoreSuccessPreview() {
    PreviewTheme {
        PaywallRestoreResultDialog(
            outcome = RestoreOutcome.Success,
            onDismiss = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 260, name = "Paywall restore failed")
@Preview(widthDp = 360, heightDp = 260, name = "Paywall restore failed RU", locale = "ru")
@Composable
private fun PaywallRestoreFailedPreview() {
    PreviewTheme {
        PaywallRestoreResultDialog(
            outcome = RestoreOutcome.Failed(
                reason = RestoreFailureReason.StoreError,
                details = "Network timeout"
            ),
            onDismiss = {}
        )
    }
}

@Preview(widthDp = 800, heightDp = 900, name = "Paywall tablet")
@Preview(widthDp = 800, heightDp = 900, name = "Paywall tablet RU", locale = "ru")
@Composable
private fun PaywallTabletPreview() {
    PreviewPaywall(
        initialState = PaywallProductsState.Loaded(previewPromoProducts),
        selectedProduct = PremiumProductId.Month,
        products = previewPromoProducts
    )
}

