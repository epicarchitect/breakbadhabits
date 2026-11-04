package epicarchitect.breakbadhabits.monetization

import kotlin.time.Instant

enum class PremiumProductId(val id: String) {
    Lifetime("premium_lifetime"),
    Month("premium_month"),
    Year("premium_year")
}

data class PremiumProduct(
    val id: PremiumProductId,
    val priceLabel: String,
    val monthlyPriceLabel: String? = null,
    val trialDays: Int? = null,
    val priceMinorUnits: Int? = null,
    val currencyCode: String = "",
    val promotion: PremiumPromotion? = null,
    val introOffer: PremiumIntroOffer? = null
)

data class PremiumPromotion(
    val basePriceLabel: String,
    val percent: Int?,
    val basePriceMinorUnits: Int? = null,
    val endDate: Instant? = null
)

data class PremiumIntroOffer(
    val priceLabel: String,
    val nextPriceLabel: String
)
