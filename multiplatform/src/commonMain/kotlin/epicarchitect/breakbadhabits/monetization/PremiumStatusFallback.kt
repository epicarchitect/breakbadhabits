package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

fun fallbackPremiumStatus(
    productId: PremiumProductId,
    now: Instant
): PremiumStatus = PremiumStatus(
    expiresAt = when (productId) {
        PremiumProductId.Month -> now + 31.days
        PremiumProductId.Year -> now + 366.days
        PremiumProductId.Lifetime -> Instant.DISTANT_FUTURE
    }
)
