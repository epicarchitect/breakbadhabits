package epicarchitect.breakbadhabits.monetization

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class PremiumStatusFallbackTest {

    private val now = Instant.fromEpochSeconds(1_800_000_000)

    @Test
    fun lifetimeFallbackIsDistantFuture() {
        val result = fallbackPremiumStatus(
            productId = PremiumProductId.Lifetime,
            now = now
        )

        assertEquals(Instant.DISTANT_FUTURE, result.expiresAt)
    }

    @Test
    fun monthlyFallbackIsNotOneDay() {
        val result = fallbackPremiumStatus(
            productId = PremiumProductId.Month,
            now = now
        )

        assertEquals(now + 31.days, result.expiresAt)
    }

    @Test
    fun yearlyFallbackCoversFullYear() {
        val result = fallbackPremiumStatus(
            productId = PremiumProductId.Year,
            now = now
        )

        assertEquals(now + 366.days, result.expiresAt)
    }
}
