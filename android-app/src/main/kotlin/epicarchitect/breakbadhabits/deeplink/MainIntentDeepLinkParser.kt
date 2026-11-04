package epicarchitect.breakbadhabits.deeplink

import android.content.Intent
import androidx.navigation3.runtime.NavKey
import epicarchitect.breakbadhabits.MainActivity
import epicarchitect.breakbadhabits.screens.habits.details.parseHabitDetailsDeepLink
import epicarchitect.breakbadhabits.screens.monetization.parsePaywallDeepLink

object MainIntentDeepLinkParser {
    fun parse(intent: Intent): List<NavKey> = buildList {
        parseHabitDetailsDeepLink(
            intent.getStringExtra(MainActivity.EXTRA_HABIT_ID)
        )?.let(::add)
        parsePaywallDeepLink(
            intent.getStringExtra(MainActivity.EXTRA_PAYWALL_SOURCE)
        )?.let(::add)
    }
}
