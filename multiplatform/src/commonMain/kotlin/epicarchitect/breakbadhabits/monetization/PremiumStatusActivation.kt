package epicarchitect.breakbadhabits.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import epicarchitect.breakbadhabits.database.PremiumStatus
import epicarchitect.breakbadhabits.infrastructure.Monetization
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun rememberPremiumIsActive(): Boolean {
    val status by Monetization.premiumStatus.collectAsState()
    return status.isActive(Clock.System.now())
}

fun PremiumStatus?.isActive(now: Instant): Boolean =
    this != null && expiresAt > now
