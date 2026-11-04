package epicarchitect.breakbadhabits.monetization

enum class PremiumProductId(val id: String) {
    Lifetime("premium_lifetime"),
    Month("premium_month"),
    Year("premium_year")
}

data class PremiumProduct(
    val id: PremiumProductId,
    val priceLabel: String,
    val monthlyPriceLabel: String? = null,
    val trialDays: Int? = null
)
