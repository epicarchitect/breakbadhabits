package epicarchitect.breakbadhabits.screens.monetization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import epicarchitect.breakbadhabits.database.PremiumOfferInfo
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

const val PremiumOfferHabitLimitKey = "habit_limit"
const val PremiumOfferRiskMapKey = "risk_map"
const val PremiumOfferHistogramKey = "histogram"

private val PremiumOfferHideDuration = 7.days

@Stable
class PremiumOfferState(
    val visible: Boolean,
    private val dismissOffer: () -> Unit
) {
    fun dismiss() = dismissOffer()
}

@Composable
fun rememberPremiumOfferState(offerId: String): PremiumOfferState {
    val offersInfo by remember {
        AppDatabase.premiumOfferInfoDao.all()
    }.collectAsState(initial = null)
    val infoLoaded = offersInfo != null
    val dismissTime = offersInfo
        ?.firstOrNull { it.id == offerId }
        ?.dismissTime
    var visible by remember(offerId, dismissTime, infoLoaded) {
        mutableStateOf(
            infoLoaded &&
                    (dismissTime == null ||
                            Clock.System.now() >= dismissTime + PremiumOfferHideDuration)
        )
    }

    LaunchedEffect(offerId, dismissTime) {
        val showAgainTime = dismissTime
            ?.plus(PremiumOfferHideDuration)
            ?: return@LaunchedEffect
        val remainingMillis = (showAgainTime - Clock.System.now()).inWholeMilliseconds
        if (remainingMillis > 0) {
            delay(remainingMillis)
        }
        visible = true
    }

    val scope = rememberCoroutineScope()
    return remember(offerId, visible, scope) {
        PremiumOfferState(
            visible = visible,
            dismissOffer = {
                visible = false
                scope.launch {
                    AppDatabase.premiumOfferInfoDao.save(
                        PremiumOfferInfo(
                            id = offerId,
                            dismissTime = Clock.System.now()
                        )
                    )
                }
            }
        )
    }
}
