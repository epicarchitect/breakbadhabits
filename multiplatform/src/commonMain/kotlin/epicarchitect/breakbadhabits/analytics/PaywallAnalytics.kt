package epicarchitect.breakbadhabits.analytics

import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator

private const val PaywallViewed = "paywall_viewed"
private const val PaywallProductSelected = "paywall_product_selected"
private const val PaywallCtaClicked = "paywall_cta_clicked"
private const val PaywallRestoreClicked = "paywall_restore_clicked"
private const val PurchaseStarted = "purchase_started"
private const val PurchaseSuccess = "purchase_success"
private const val PurchaseFailed = "purchase_failed"
private const val PremiumActivated = "premium_activated"
private const val IsPremium = "is_premium"
private const val PremiumSource = "premium_source"
private const val PremiumSandbox = "premium_sandbox"
private const val Source = "source"
private const val ProductId = "product_id"
private const val PriceMinorUnits = "price_minor_units"
private const val AmountLabel = "amount_label"
private const val DiscountMinorUnits = "discount_minor_units"
private const val Currency = "currency"
private const val Sandbox = "sandbox"
private const val AcknowledgementState = "acknowledgement_state"
private const val Reason = "reason"

object PaywallAnalytics {
    fun paywallViewed(source: String) {
        AnalyticsOperator.logEvent(
            name = PaywallViewed,
            parameters = mapOf(Source to source)
        )
    }

    fun paywallProductSelected(productId: String) {
        AnalyticsOperator.logEvent(
            name = PaywallProductSelected,
            parameters = mapOf(ProductId to productId)
        )
    }

    fun paywallCtaClicked(productId: String) {
        AnalyticsOperator.logEvent(
            name = PaywallCtaClicked,
            parameters = mapOf(ProductId to productId)
        )
    }

    fun paywallRestoreClicked() {
        AnalyticsOperator.logEvent(name = PaywallRestoreClicked)
    }

    fun purchaseStarted(productId: String) {
        AnalyticsOperator.logEvent(
            name = PurchaseStarted,
            parameters = mapOf(ProductId to productId)
        )
    }

    fun purchaseSucceeded(
        productId: String,
        priceMinorUnits: Int? = null,
        amountLabel: String? = null,
        discountMinorUnits: Int? = null,
        currency: String? = null,
        sandbox: Boolean? = null
    ) {
        AnalyticsOperator.logEvent(
            name = PurchaseSuccess,
            parameters = buildMap {
                put(ProductId, productId)
                priceMinorUnits?.let { put(PriceMinorUnits, it) }
                amountLabel?.let { put(AmountLabel, it) }
                discountMinorUnits?.let { put(DiscountMinorUnits, it) }
                currency?.let { put(Currency, it) }
                sandbox?.let { put(Sandbox, it.toString()) }
            }
        )
    }

    fun purchaseFailed(productId: String, reason: String?) {
        AnalyticsOperator.logEvent(
            name = PurchaseFailed,
            parameters = mapOf(
                ProductId to productId,
                Reason to (reason ?: "unknown")
            )
        )
    }

    fun premiumActivated(
        source: String,
        productId: String? = null,
        priceMinorUnits: Int? = null,
        amountLabel: String? = null,
        discountMinorUnits: Int? = null,
        currency: String? = null,
        sandbox: Boolean? = null,
        acknowledgementState: String? = null
    ) {
        AnalyticsOperator.logEvent(
            name = PremiumActivated,
            parameters = buildMap {
                put(Source, source)
                productId?.let { put(ProductId, it) }
                priceMinorUnits?.let { put(PriceMinorUnits, it) }
                amountLabel?.let { put(AmountLabel, it) }
                discountMinorUnits?.let { put(DiscountMinorUnits, it) }
                currency?.let { put(Currency, it) }
                sandbox?.let { put(Sandbox, it.toString()) }
                acknowledgementState?.let { put(AcknowledgementState, it) }
            }
        )
    }

    fun setPremium(
        isPremium: Boolean,
        source: String? = null,
        sandbox: Boolean? = null
    ) {
        AnalyticsOperator.setUserProperty(IsPremium, isPremium.toString())
        AnalyticsOperator.setUserProperty(PremiumSource, source)
        AnalyticsOperator.setUserProperty(PremiumSandbox, sandbox?.toString())
    }
}
