package epicarchitect.breakbadhabits.screens.monetization

import epicarchitect.breakbadhabits.database.PremiumCache
import epicarchitect.breakbadhabits.monetization.MonetizationEnvironment
import epicarchitect.breakbadhabits.monetization.PremiumIntroOffer
import epicarchitect.breakbadhabits.monetization.PremiumProduct
import epicarchitect.breakbadhabits.monetization.PremiumProductId
import epicarchitect.breakbadhabits.monetization.PremiumPromotion
import epicarchitect.breakbadhabits.monetization.PurchaseOutcome
import epicarchitect.breakbadhabits.monetization.RestoreOutcome
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import kotlinx.coroutines.flow.MutableStateFlow

internal class PreviewMonetization(
    private val products: List<PremiumProduct>,
    override val purchasesAvailable: Boolean
) : MonetizationEnvironment {
    override val store = PurchaseStore.RuStore
    override val premiumCache = MutableStateFlow<PremiumCache?>(null)

    override suspend fun loadProducts(): List<PremiumProduct> = products

    override suspend fun purchase(
        productId: PremiumProductId,
        source: String?
    ): PurchaseOutcome = PurchaseOutcome.Success

    override suspend fun restorePurchases(): RestoreOutcome = RestoreOutcome.Success

    override fun grantTestPremium() = Unit
}

internal fun previewYearProduct(
    promotion: PremiumPromotion? = null,
    introOffer: PremiumIntroOffer? = null
) = PremiumProduct(
    id = PremiumProductId.Year,
    priceLabel = "2 990 ₽",
    monthlyPriceLabel = "249 ₽",
    priceMinorUnits = 299_000,
    promotion = promotion,
    introOffer = introOffer
)

internal fun previewMonthProduct(
    trialDays: Int? = null,
    promotion: PremiumPromotion? = null,
    introOffer: PremiumIntroOffer? = null,
    gracePeriodDays: Int? = null,
    holdPeriodDays: Int? = null
) = PremiumProduct(
    id = PremiumProductId.Month,
    priceLabel = "490 ₽",
    trialDays = trialDays,
    priceMinorUnits = 49_000,
    promotion = promotion,
    introOffer = introOffer,
    gracePeriodDays = gracePeriodDays,
    holdPeriodDays = holdPeriodDays
)

internal fun previewLifetimeProduct(
    promotion: PremiumPromotion? = null
) = PremiumProduct(
    id = PremiumProductId.Lifetime,
    priceLabel = "5 990 ₽",
    priceMinorUnits = 599_000,
    promotion = promotion
)

internal fun previewPromotion(
    basePriceLabel: String,
    basePriceMinorUnits: Int,
    discountLabel: String,
    percent: Int
) = PremiumPromotion(
    basePriceLabel = basePriceLabel,
    discountLabel = discountLabel,
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
            discountLabel = "490 ₽",
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
            discountLabel = "490 ₽",
            percent = 50
        ),
        gracePeriodDays = 3,
        holdPeriodDays = 5
    ),
    previewLifetimeProduct()
)

internal val previewYearPromoProducts = listOf(
    previewYearProduct(
        promotion = previewPromotion(
            basePriceLabel = "3 990 ₽",
            basePriceMinorUnits = 399_000,
            discountLabel = "1 000 ₽",
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
            discountLabel = "4 000 ₽",
            percent = 40
        )
    )
)
