package epicarchitect.breakbadhabits.screens.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.monetization.PremiumIntroOffer
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PremiumPromotion
import epicarchitect.breakbadhabits.monetization.PurchaseOutcome
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import kotlinx.coroutines.flow.MutableStateFlow

internal class PreviewMonetization(
    private val products: List<PremiumProduct>,
    override val purchasesAvailable: Boolean,
    override val store: PurchaseStore
) : MonetizationEnvironment {
    override val premiumStatus = MutableStateFlow<PremiumStatus?>(null)

    override suspend fun loadProducts(): List<PremiumProduct> = products

    override suspend fun purchase(
        productId: PremiumProductId
    ): PurchaseOutcome = PurchaseOutcome.Success

    override suspend fun restorePurchases(): RestoreOutcome = RestoreOutcome.Success
}

internal fun previewYearProduct(
    promotion: PremiumPromotion? = null,
    introOffer: PremiumIntroOffer? = null
) = PremiumProduct(
    id = PremiumProductId.Year,
    priceLabel = "2 990 ₽",
    monthlyPriceLabel = "249 ₽",
    priceMinorUnits = 299_000,
    currencyCode = "RUB",
    promotion = promotion,
    introOffer = introOffer
)

internal fun previewMonthProduct(
    trialDays: Int? = null,
    promotion: PremiumPromotion? = null,
    introOffer: PremiumIntroOffer? = null
) = PremiumProduct(
    id = PremiumProductId.Month,
    priceLabel = "490 ₽",
    trialDays = trialDays,
    priceMinorUnits = 49_000,
    currencyCode = "RUB",
    promotion = promotion,
    introOffer = introOffer
)

internal fun previewLifetimeProduct(
    promotion: PremiumPromotion? = null
) = PremiumProduct(
    id = PremiumProductId.Lifetime,
    priceLabel = "5 990 ₽",
    priceMinorUnits = 599_000,
    currencyCode = "RUB",
    promotion = promotion
)

internal fun previewPromotion(
    basePriceLabel: String,
    basePriceMinorUnits: Int,
    percent: Int
) = PremiumPromotion(
    basePriceLabel = basePriceLabel,
    percent = percent,
    basePriceMinorUnits = basePriceMinorUnits,
)

internal val previewTrialProducts = listOf(
    previewYearProduct(),
    previewMonthProduct(trialDays = 3),
    previewLifetimeProduct()
)

internal val previewPromoProducts = listOf(
    previewYearProduct(),
    previewMonthProduct(
        trialDays = 3,
        promotion = previewPromotion(
            basePriceLabel = "980 ₽",
            basePriceMinorUnits = 98_000,
            percent = 50
        )
    ),
    previewLifetimeProduct()
)

internal val previewIntroProducts = listOf(
    previewYearProduct(
        introOffer = PremiumIntroOffer(
            priceLabel = "1 990 ₽",
            nextPriceLabel = "2 990 ₽"
        )
    ),
    previewMonthProduct(
        trialDays = 3,
        introOffer = PremiumIntroOffer(
            priceLabel = "149 ₽",
            nextPriceLabel = "490 ₽"
        ),
        promotion = previewPromotion(
            basePriceLabel = "980 ₽",
            basePriceMinorUnits = 98_000,
            percent = 50
        )
    ),
    previewLifetimeProduct()
)

internal val previewYearPromoProducts = listOf(
    previewYearProduct(
        promotion = previewPromotion(
            basePriceLabel = "3 990 ₽",
            basePriceMinorUnits = 399_000,
            percent = 25
        )
    ),
    previewMonthProduct(trialDays = 3),
    previewLifetimeProduct()
)

internal val previewLifetimePromoProducts = listOf(
    previewYearProduct(),
    previewMonthProduct(trialDays = 3),
    previewLifetimeProduct(
        promotion = previewPromotion(
            basePriceLabel = "9 990 ₽",
            basePriceMinorUnits = 999_000,
            percent = 40
        )
    )
)
