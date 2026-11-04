package epicarchitect.breakbadhabits.analytics

import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator

private const val PaywallViewed = "paywall_viewed"
private const val PaywallProductSelected = "paywall_product_selected"
private const val PaywallCtaClicked = "paywall_cta_clicked"
private const val PurchaseStarted = "purchase_started"
private const val PurchaseSuccess = "purchase_success"
private const val PurchaseFailed = "purchase_failed"
private const val PremiumActivated = "premium_activated"
private const val IsPremium = "is_premium"
private const val PremiumSource = "premium_source"
private const val Source = "source"
private const val ProductId = "product_id"
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

    fun purchaseStarted(productId: String) {
        AnalyticsOperator.logEvent(
            name = PurchaseStarted,
            parameters = mapOf(ProductId to productId)
        )
    }

    fun purchaseSucceeded(productId: String) {
        AnalyticsOperator.logEvent(
            name = PurchaseSuccess,
            parameters = mapOf(ProductId to productId)
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

    fun premiumActivated(source: String) {
        AnalyticsOperator.logEvent(
            name = PremiumActivated,
            parameters = mapOf(Source to source)
        )
    }

    fun setPremium(isPremium: Boolean, source: String? = null) {
        AnalyticsOperator.setUserProperty(IsPremium, isPremium.toString())
        AnalyticsOperator.setUserProperty(PremiumSource, source)
    }
}
