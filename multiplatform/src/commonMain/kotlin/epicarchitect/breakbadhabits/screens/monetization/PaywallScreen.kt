package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.paywall_cta_trial
import epicarchitect.breakbadhabits.resources.paywall_debug_stub
import epicarchitect.breakbadhabits.resources.paywall_included_habits
import epicarchitect.breakbadhabits.resources.paywall_lifetime_note
import epicarchitect.breakbadhabits.resources.paywall_loading
import epicarchitect.breakbadhabits.resources.paywall_manage_note
import epicarchitect.breakbadhabits.resources.paywall_offer_headline
import epicarchitect.breakbadhabits.resources.paywall_offer_included_title
import epicarchitect.breakbadhabits.resources.paywall_offer_lifetime
import epicarchitect.breakbadhabits.resources.paywall_offer_month
import epicarchitect.breakbadhabits.resources.paywall_offer_subhead
import epicarchitect.breakbadhabits.resources.paywall_offer_subhead_unavailable
import epicarchitect.breakbadhabits.resources.paywall_offer_year
import epicarchitect.breakbadhabits.resources.paywall_per_month
import epicarchitect.breakbadhabits.resources.paywall_plan_best
import epicarchitect.breakbadhabits.resources.paywall_plan_once
import epicarchitect.breakbadhabits.resources.paywall_premium_active_message
import epicarchitect.breakbadhabits.resources.paywall_premium_active_title
import epicarchitect.breakbadhabits.resources.paywall_price_per_month
import epicarchitect.breakbadhabits.resources.paywall_price_per_year
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
import epicarchitect.breakbadhabits.resources.back
import epicarchitect.breakbadhabits.resources.paywall_title
import epicarchitect.breakbadhabits.resources.paywall_trial_badge
import epicarchitect.breakbadhabits.resources.paywall_trial_charge
import epicarchitect.breakbadhabits.resources.paywall_trial_reminder
import epicarchitect.breakbadhabits.resources.paywall_trial_today
import epicarchitect.breakbadhabits.resources.paywall_unavailable_message
import epicarchitect.breakbadhabits.resources.paywall_unavailable_title
import epicarchitect.breakbadhabits.resources.paywall_value_cta
import epicarchitect.breakbadhabits.resources.paywall_value_days_label
import epicarchitect.breakbadhabits.resources.paywall_value_headline
import epicarchitect.breakbadhabits.resources.paywall_value_subhead
import epicarchitect.breakbadhabits.resources.paywall_value_trust_local
import epicarchitect.breakbadhabits.resources.paywall_value_trust_no_ads
import epicarchitect.breakbadhabits.screens.root.LocalRootBackStack
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.TERMS_OF_USE_URL
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState


private val OfferOrder = listOf(
    PremiumProductId.Year,
    PremiumProductId.Month,
    PremiumProductId.Lifetime
)

@Composable
fun PaywallScreen(source: String = "unknown") {
    val monetization = Monetization
    val state = rememberPaywallScreenState(source)
    val premiumCache by monetization.premiumCache.collectAsState()
    val backStack = LocalRootBackStack.current
    var showOffer by rememberSaveable { mutableStateOf(false) }
    var progressDays by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(state) {
        if (state.purchasesAvailable) progressDays = loadProgressDays()
    }

    val listState = rememberLazyListState()
    val bottomInset = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(bottom = bottomInset),
        state = listState
    ) {
        item(key = "app_bar") {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = stringResource(Res.string.paywall_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
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
        }

        item(key = "content") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                ) {
                    if (premiumCache.isPremium) {
                        PaywallActiveBlock()
                        PaywallLegalFooter()
                    } else if (showOffer.not()) {
                        PaywallValueStep(
                            progressDays = progressDays,
                            onContinue = {
                                PaywallAnalytics.paywallCtaClicked("see_plans")
                                showOffer = true
                            }
                        )
                    } else {
                        PaywallOfferStep(state)
                        PaywallLegalFooter()

                        if (isDebug) {
                            TextButton(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                onClick = monetization::grantTestPremium
                            ) {
                                Text(text = stringResource(Res.string.paywall_test_premium))
                            }
                        }
                    }

                    Spacer(Modifier.height(32.dp))
    
                }
            }
        }
    }
}

@Composable
private fun PaywallLegalFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            TextButton(onClick = { UrlOpener.openUrl(PRIVACY_POLICY_URL) }) {
                Text(text = stringResource(Res.string.paywall_privacy))
            }
            TextButton(onClick = { UrlOpener.openUrl(TERMS_OF_USE_URL) }) {
                Text(text = stringResource(Res.string.paywall_terms))
            }
        }
        Text(
            text = stringResource(Res.string.paywall_manage_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PaywallValueStep(
    progressDays: Int?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        progressDays?.takeIf { it > 0 }?.let {
            ProgressHero(days = it)
        }

        Text(
            text = stringResource(Res.string.paywall_value_headline),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.paywall_value_subhead),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(20.dp))

        Spacer(Modifier.height(16.dp))

        TrustRow(stringResource(Res.string.paywall_value_trust_local))
        TrustRow(stringResource(Res.string.paywall_value_trust_no_ads))

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            onClick = onContinue
        ) {
            Text(text = stringResource(Res.string.paywall_value_cta))
        }
    }
}

@Composable
private fun PaywallOfferStep(state: PaywallScreenState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.paywall_offer_headline),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(
                if (state.purchasesAvailable) Res.string.paywall_offer_subhead
                else Res.string.paywall_offer_subhead_unavailable
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        IncludedBlock()

        if ((state.productsState as? PaywallProductsState.Loaded)?.isDebugStub == true) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.paywall_debug_stub),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(20.dp))

        if (state.purchasesAvailable.not()) {
            UnavailableBlock()
            return@Column
        }

        when (val productsState = state.productsState) {
            PaywallProductsState.Loading -> LoadingBlock()

            PaywallProductsState.Error -> ProductsErrorBlock(state)

            is PaywallProductsState.Loaded -> {
                if (productsState.products.isEmpty()) {
                    ProductsErrorBlock(state)
                } else {
                    LoadedOfferBlock(
                        state = state,
                        products = productsState.products
                    )
                }
            }
        }
    }
}

@Composable
private fun IncludedBlock() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.paywall_offer_included_title),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(Modifier.height(8.dp))

            Column {
                IncludedItem(
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Filled.CheckCircle,
                    label = stringResource(Res.string.paywall_included_habits)
                )
            }
        }
    }
}

@Composable
private fun IncludedItem(
    modifier: Modifier,
    icon: ImageVector,
    label: String
) {
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LoadedOfferBlock(
    state: PaywallScreenState,
    products: List<PremiumProduct>
) {
    val selectedProduct = products.find { it.id == state.selectedProductId }
    val trialDays = selectedProduct?.trialDays

    OfferOrder.forEach { productId ->
        ProductCard(
            productId = productId,
            product = products.find { it.id == productId },
            selected = state.selectedProductId == productId,
            onSelect = { state.selectProduct(productId) }
        )
        Spacer(Modifier.height(8.dp))
    }

    Spacer(Modifier.height(12.dp))

    if (trialDays != null) {
        TrialBadge(trialDays)
        Spacer(Modifier.height(12.dp))
        TrialTimeline(
            trialDays = trialDays,
            chargeLabel = priceText(selectedProduct.id, selectedProduct)
        )
    }

    ActionMessage(state.actionState)

    Spacer(Modifier.height(16.dp))

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = selectedProduct != null && state.actionState.isInProgress().not(),
        onClick = state::purchase
    ) {
        if (state.actionState is PaywallActionState.Purchasing) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = stringResource(
                    if (trialDays != null) {
                        Res.string.paywall_cta_trial
                    } else {
                        Res.string.paywall_purchase
                    }
                )
            )
        }
    }

    Spacer(Modifier.height(8.dp))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(
            if (state.selectedProductId == PremiumProductId.Lifetime) {
                Res.string.paywall_lifetime_note
            } else {
                Res.string.paywall_renewal_note
            }
        ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        enabled = state.actionState.isInProgress().not(),
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
private fun ProgressHero(days: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(164.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = days.toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = stringResource(Res.string.paywall_value_days_label),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun TrustRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProductCard(
    productId: PremiumProductId,
    product: PremiumProduct?,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = selected, onClick = onSelect)

            Spacer(Modifier.width(4.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(productId.titleResource()),
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (productId == PremiumProductId.Year) {
                        Spacer(Modifier.width(8.dp))
                        BestValueBadge()
                    }
                }

                product?.monthlyPriceLabel?.let { monthlyPriceLabel ->
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = stringResource(Res.string.paywall_per_month, monthlyPriceLabel),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (productId == PremiumProductId.Lifetime) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = stringResource(Res.string.paywall_plan_once),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = priceText(productId, product),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
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

@Composable
private fun BestValueBadge() {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = stringResource(Res.string.paywall_plan_best),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun TrialBadge(trialDays: Int) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = stringResource(Res.string.paywall_trial_badge, trialDays),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun TrialTimeline(
    trialDays: Int,
    chargeLabel: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        TimelineRow(stringResource(Res.string.paywall_trial_today))
        TimelineRow(stringResource(Res.string.paywall_trial_reminder, trialDays))
        TimelineRow(stringResource(Res.string.paywall_trial_charge, chargeLabel))
    }
}

@Composable
private fun TimelineRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
private fun UnavailableBlock() {
    Surface(
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(Res.string.paywall_unavailable_title),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(Res.string.paywall_unavailable_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private suspend fun loadProgressDays(): Int? = runCatching {
    val now = Clock.System.now()
    AppDatabase.habitsDao.habits().first()
        .mapNotNull { habit ->
            AppDatabase.habitEventRecordsDao
                .recordByHabitIdAndMaxEndTime(habit.id)
                .first()
                ?.abstinence(now)
        }
        .maxOrNull()
        ?.inWholeDays
        ?.toInt()
        ?.coerceAtLeast(0)
}.getOrNull()

private fun PaywallActionState.isInProgress() =
    this is PaywallActionState.Purchasing || this is PaywallActionState.Restoring

private fun PremiumProductId.titleResource(): StringResource = when (this) {
    PremiumProductId.Lifetime -> Res.string.paywall_offer_lifetime
    PremiumProductId.Month -> Res.string.paywall_offer_month
    PremiumProductId.Year -> Res.string.paywall_offer_year
}

private fun PaywallMessage.messageResource(): StringResource = when (this) {
    PaywallMessage.PurchaseCancelled -> Res.string.paywall_purchase_cancelled
    PaywallMessage.PurchaseFailed -> Res.string.paywall_purchase_error
    PaywallMessage.RestoreFailed -> Res.string.paywall_restore_error
    PaywallMessage.NothingFound -> Res.string.paywall_restore_nothing
}
