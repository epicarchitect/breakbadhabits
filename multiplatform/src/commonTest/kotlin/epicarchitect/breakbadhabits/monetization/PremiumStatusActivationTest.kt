package epicarchitect.breakbadhabits.monetization

import epicarchitect.breakbadhabits.database.PremiumStatus
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

class PremiumStatusActivationTest {

    private val now = Instant.fromEpochSeconds(1_800_000_000)

    @Test
    fun futureStatusIsActive() {
        assertTrue(PremiumStatus(expiresAt = now + 1.days).isActive(now))
    }

    @Test
    fun expiredStatusIsInactive() {
        assertFalse(PremiumStatus(expiresAt = now - 1.days).isActive(now))
    }

    @Test
    fun missingStatusIsInactive() {
        assertFalse((null as PremiumStatus?).isActive(now))
    }
}
