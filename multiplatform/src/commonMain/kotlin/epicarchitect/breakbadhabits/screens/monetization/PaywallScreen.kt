package epicarchitect.breakbadhabits.screens.monetization

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epicarchitect.breakbadhabits.format.formatPriceMinorUnits
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PurchaseFailure
import epicarchitect.breakbadhabits.monetization.PurchaseFailureReason
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.monetization.RU_STORE_APP_URL
import epicarchitect.breakbadhabits.monetization.RestoreFailureReason
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import epicarchitect.breakbadhabits.monetization.rememberPremiumIsActive
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.app_name
import epicarchitect.breakbadhabits.resources.common_ok
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
import epicarchitect.breakbadhabits.resources.paywall_googleplay_card_message
import epicarchitect.breakbadhabits.resources.paywall_googleplay_export_hint
import epicarchitect.breakbadhabits.resources.paywall_googleplay_message
import epicarchitect.breakbadhabits.resources.paywall_googleplay_title
import epicarchitect.breakbadhabits.resources.paywall_intro_offer
import epicarchitect.breakbadhabits.resources.paywall_ios_message
import epicarchitect.breakbadhabits.resources.paywall_ios_title
import epicarchitect.breakbadhabits.resources.paywall_offer_lifetime
import epicarchitect.breakbadhabits.resources.paywall_offer_month
import epicarchitect.breakbadhabits.resources.paywall_offer_year
import epicarchitect.breakbadhabits.resources.paywall_open_rustore
import epicarchitect.breakbadhabits.resources.paywall_per_month
import epicarchitect.breakbadhabits.resources.paywall_plan_savings
import epicarchitect.breakbadhabits.resources.paywall_plan_unit_month
import epicarchitect.breakbadhabits.resources.paywall_plan_unit_year
import epicarchitect.breakbadhabits.resources.paywall_privacy
import epicarchitect.breakbadhabits.resources.paywall_products_error
import epicarchitect.breakbadhabits.resources.paywall_products_error_reason
import epicarchitect.breakbadhabits.resources.paywall_promotion_until
import epicarchitect.breakbadhabits.resources.paywall_purchase_error
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_already_active
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_blocked
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_code
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_invoice_id
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_network
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_no_payment_method
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_order_id
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_payment_declined
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_payment_system
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_product
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_purchase_id
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_sign_in
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_support
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_technical
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_title
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_unavailable
import epicarchitect.breakbadhabits.resources.paywall_purchase_error_version
import epicarchitect.breakbadhabits.resources.paywall_restore
import epicarchitect.breakbadhabits.resources.paywall_restore_failed_title
import epicarchitect.breakbadhabits.resources.paywall_restore_no_purchases
import epicarchitect.breakbadhabits.resources.paywall_restore_store_error
import epicarchitect.breakbadhabits.resources.paywall_restore_success_message
import epicarchitect.breakbadhabits.resources.paywall_restore_success_title
import epicarchitect.breakbadhabits.resources.paywall_restore_unknown_error
import epicarchitect.breakbadhabits.resources.paywall_retry
import epicarchitect.breakbadhabits.resources.paywall_tariff_lifetime_note
import epicarchitect.breakbadhabits.resources.paywall_tariff_month_note
import epicarchitect.breakbadhabits.resources.paywall_terms
import epicarchitect.breakbadhabits.resources.paywall_trial_badge
import epicarchitect.breakbadhabits.resources.paywall_unavailable_message
import epicarchitect.breakbadhabits.resources.paywall_unavailable_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AdaptiveContentWidth
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.PremiumShimmerLabel
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.PrimaryButton
import epicarchitect.breakbadhabits.uikit.appGlow
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.TERMS_OF_USE_URL
import epicarchitect.breakbadhabits.url.openUrl
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt
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
fun PaywallScreen() {
    val monetization = Monetization
    val state = rememberPaywallScreenState()
    val isPremium = rememberPremiumIsActive()
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

    AdaptiveContent(
        modifier = Modifier
            .fillMaxSize()
            .appGlow()
            .statusBarsPadding(),
        width = AdaptiveContentWidth.Reading
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
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
            if (state.purchasesAvailable) {
                AnimatedContent(
                    targetState = state.productsState,
                    transitionSpec = { paywallContentTransition() },
                    contentAlignment = Alignment.TopCenter,
                    label = "paywallFooter"
                ) { productsState ->
                    when (productsState) {
                        PaywallProductsState.Loading -> PaywallFooterShimmer()
                        is PaywallProductsState.Loaded -> PaywallLegalFooter(state = state)
                        PaywallProductsState.Error -> Box(Modifier)
                    }
                }
            }
            Spacer(Modifier.height(20.dp + navigationBarBottom))
        }
    }
    state.restoreOutcome?.let { outcome ->
        PaywallRestoreResultDialog(
            outcome = outcome,
            onDismiss = state::dismissRestoreOutcome
        )
    }
    (state.actionState as? PaywallActionState.Failed)?.let { failed ->
        PaywallPurchaseErrorDialog(
            failure = failed.failure,
            onDismiss = state::dismissPurchaseError
        )
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
    PaywallAlertDialog(
        title = title,
        message = message,
        onDismiss = onDismiss
    )
}

@Composable
private fun PaywallPurchaseErrorDialog(
    failure: PurchaseFailure,
    onDismiss: () -> Unit
) {
    PaywallAlertDialog(
        title = stringResource(Res.string.paywall_purchase_error_title),
        message = stringResource(failure.reason.messageResource()),
        onDismiss = onDismiss,
        details = { PaywallPurchaseErrorDetails(failure = failure) }
    )
}

@Composable
private fun PaywallPurchaseErrorDetails(failure: PurchaseFailure) {
    val productLabel = stringResource(Res.string.paywall_purchase_error_product)
    val purchaseLabel = stringResource(Res.string.paywall_purchase_error_purchase_id)
    val orderLabel = stringResource(Res.string.paywall_purchase_error_order_id)
    val invoiceLabel = stringResource(Res.string.paywall_purchase_error_invoice_id)
    val codeLabel = stringResource(Res.string.paywall_purchase_error_code)
    val technicalLabel = stringResource(Res.string.paywall_purchase_error_technical)
    val versionLabel = stringResource(Res.string.paywall_purchase_error_version)
    val rows = buildList {
        failure.productId?.takeIf { it.isNotBlank() }?.let { add(productLabel to it) }
        failure.purchaseId?.takeIf { it.isNotBlank() }?.let { add(purchaseLabel to it) }
        failure.orderId?.takeIf { it.isNotBlank() }?.let { add(orderLabel to it) }
        failure.invoiceId?.takeIf { it.isNotBlank() }?.let { add(invoiceLabel to it) }
        failure.errorCode?.takeIf { it.isNotBlank() }?.let { add(codeLabel to it) }
        failure.technicalMessage?.takeIf { it.isNotBlank() }?.let { add(technicalLabel to it) }
        appVersion.takeIf { it.isNotBlank() }?.let { add(versionLabel to it) }
    }
    if (rows.isEmpty()) return

    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.60f)
    )
    Spacer(Modifier.height(12.dp))
    Text(
        text = stringResource(Res.string.paywall_purchase_error_support),
        style = MaterialTheme.typography.labelLarge
    )
    Spacer(Modifier.height(6.dp))
    SelectionContainer {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            rows.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        modifier = Modifier.width(88.dp),
                        text = label,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = value,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = if (label == technicalLabel) 3 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PurchaseFailureReason.messageResource(): StringResource = when (this) {
    PurchaseFailureReason.Network -> Res.string.paywall_purchase_error_network
    PurchaseFailureReason.PaymentDeclined -> Res.string.paywall_purchase_error_payment_declined
    PurchaseFailureReason.Unavailable -> Res.string.paywall_purchase_error_unavailable
    PurchaseFailureReason.AlreadyActive -> Res.string.paywall_purchase_error_already_active
    PurchaseFailureReason.NoPaymentMethod -> Res.string.paywall_purchase_error_no_payment_method
    PurchaseFailureReason.SignInRequired -> Res.string.paywall_purchase_error_sign_in
    PurchaseFailureReason.Blocked -> Res.string.paywall_purchase_error_blocked
    PurchaseFailureReason.PaymentSystem -> Res.string.paywall_purchase_error_payment_system
    PurchaseFailureReason.Unknown -> Res.string.paywall_purchase_error
}

@Composable
private fun PaywallAlertDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    details: (@Composable () -> Unit)? = null
) {
    Dialog(onDismiss = onDismiss) {
        Column(Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
            details?.let {
                Spacer(Modifier.height(12.dp))
                it()
            }
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
    val iconShape = MaterialTheme.shapes.medium
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
            Text(
                text = stringResource(Res.string.app_name),
                color = scheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = scheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
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
            if (state.store == PurchaseStore.GooglePlay) {
                Spacer(Modifier.height(12.dp))
                PaywallGooglePlayBridge(
                    onOpenRuStore = { openUrl(RU_STORE_APP_URL) }
                )
            } else {
                PaywallStoreNotice(store = state.store)
            }
        } else if (LocalInspectionMode.current) {
            PaywallPlansContent(
                state = state,
                productsState = state.productsState
            )
        } else {
            AnimatedContent(
                targetState = state.productsState,
                transitionSpec = { paywallContentTransition() },
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
    val shape = MaterialTheme.shapes.medium
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
            .toLocalDateTime(AppTimeZone)
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
                        style = MaterialTheme.typography.titleSmall,
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
                        scheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.bodySmall
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
        shape = MaterialTheme.shapes.medium,
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
            .height(48.dp),
        shape = MaterialTheme.shapes.extraLarge
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

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = MaterialTheme.shapes.large,
        color = scheme.onSurface.copy(alpha = 0.035f),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.60f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        color = scheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = Icons.Filled.ErrorOutline,
                    contentDescription = null,
                    tint = scheme.primary
                )
            }
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(Res.string.paywall_products_error),
                color = scheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(Res.string.paywall_products_error_reason),
                color = scheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(18.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.paywall_retry),
                onClick = state::loadProducts
            )
        }
    }
}

@Composable
private fun PaywallGooglePlayBridge(
    onOpenRuStore: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.large

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        scheme.primary.copy(alpha = 0.16f),
                        scheme.onSurface.copy(alpha = 0.035f)
                    )
                ),
                shape = shape
            )
            .border(
                border = BorderStroke(1.dp, scheme.primary.copy(alpha = 0.30f)),
                shape = shape
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(27.dp),
                    imageVector = Icons.Filled.ShoppingBag,
                    contentDescription = null,
                    tint = Color(0xFF1B1B20)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.paywall_googleplay_card_message),
                color = scheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(scheme.primary.copy(alpha = 0.08f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    modifier = Modifier.size(17.dp),
                    imageVector = Icons.Filled.FileDownload,
                    contentDescription = null,
                    tint = scheme.primary
                )
                Text(
                    text = stringResource(Res.string.paywall_googleplay_export_hint),
                    color = scheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.paywall_open_rustore),
                onClick = onOpenRuStore
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
        shape = MaterialTheme.shapes.medium,
        color = scheme.onSurface.copy(alpha = 0.04f),
        border = BorderStroke(1.dp, scheme.outlineVariant.copy(alpha = 0.50f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 11.dp, vertical = 10.dp)
        ) {
            Text(
                text = stringResource(title),
                color = scheme.onBackground,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = stringResource(message),
                color = scheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
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
        if (state.purchasesAvailable) {
            val product = selectedProduct
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                AnimatedContent(
                    modifier = Modifier.fillMaxWidth(),
                    targetState = state.productsState,
                    transitionSpec = { paywallContentTransition() },
                    contentAlignment = Alignment.Center,
                    label = "paywallCta"
                ) { productsState ->
                    if (productsState is PaywallProductsState.Loading) {
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
}

@Composable
private fun PaywallFooterShimmer() {
    val progress = rememberPaywallShimmerProgress()

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        PaywallFooterShimmerBlock(
            text = stringResource(Res.string.paywall_restore),
            progress = progress
        )
        PaywallFooterSeparator()
        PaywallFooterShimmerBlock(
            text = stringResource(Res.string.paywall_terms),
            progress = progress
        )
        PaywallFooterSeparator()
        PaywallFooterShimmerBlock(
            text = stringResource(Res.string.paywall_privacy),
            progress = progress
        )
    }
}

@Composable
private fun PaywallFooterShimmerBlock(text: String, progress: Float) {
    Box {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            text = text,
            color = Color.Transparent,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
        SkeletonBlock(
            progress = progress,
            modifier = Modifier.matchParentSize(),
            shape = RoundedCornerShape(8.dp)
        )
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
        PaywallFooterLink(
            text = stringResource(Res.string.paywall_restore),
            onClick = state::restorePurchases
        )
        PaywallFooterSeparator()
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
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1
    )
}

@Composable
private fun PaywallFooterSeparator() {
    Text(
        modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp),
        text = "·",
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
private fun PaywallContinueButton(
    label: String,
    enabled: Boolean,
    purchasing: Boolean,
    onClick: () -> Unit
) {
    PrimaryButton(
        modifier = Modifier.fillMaxWidth(),
        text = label,
        enabled = enabled,
        loading = purchasing,
        onClick = onClick
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

    return formatPriceMinorUnits(savings, year.currencyCode)
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

private fun paywallContentTransition() =
    fadeIn(
        animationSpec = tween(
            durationMillis = 240,
            easing = FastOutSlowInEasing
        )
    ) togetherWith fadeOut(animationSpec = tween(durationMillis = 140))

private fun PremiumProductId.titleResource(): StringResource = when (this) {
    PremiumProductId.Lifetime -> Res.string.paywall_offer_lifetime
    PremiumProductId.Month -> Res.string.paywall_offer_month
    PremiumProductId.Year -> Res.string.paywall_offer_year
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
        purchasesAvailable = false,
        store = PurchaseStore.GooglePlay
    )
}

@Composable
private fun PreviewPaywall(
    initialState: PaywallProductsState,
    selectedProduct: PremiumProductId?,
    products: List<PremiumProduct>,
    purchasesAvailable: Boolean = true,
    store: PurchaseStore = PurchaseStore.RuStore
) {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(initialState, products, selectedProduct, purchasesAvailable, store) {
        PaywallScreenState(
            monetization = PreviewMonetization(
                products = products,
                purchasesAvailable = purchasesAvailable,
                store = store
            ),
            coroutineScope = coroutineScope,
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

@Preview(widthDp = 412, heightDp = 80, name = "Paywall footer shimmer")
@Preview(widthDp = 412, heightDp = 80, name = "Paywall footer shimmer RU", locale = "ru")
@Composable
private fun PaywallFooterShimmerPreview() {
    PreviewTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            PaywallFooterShimmer()
        }
    }
}

@Preview(widthDp = 400, heightDp = 620, name = "Paywall purchase error")
@Preview(widthDp = 400, heightDp = 620, name = "Paywall purchase error RU", locale = "ru")
@Composable
private fun PaywallPurchaseErrorPreview() {
    PreviewTheme {
        PaywallPurchaseErrorDialog(
            failure = PurchaseFailure(
                reason = PurchaseFailureReason.PaymentDeclined,
                productId = "premium_year",
                purchaseId = "purchase-8f3c2a",
                orderId = "order-4d21b7",
                invoiceId = "invoice-91a0c5",
                errorCode = "PAYMENT_DECLINED",
                technicalMessage = "The payment was declined by the issuer."
            ),
            onDismiss = {}
        )
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

