package epicarchitect.breakbadhabits

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import epicarchitect.breakbadhabits.deeplink.MainIntentDeepLinkParser
import epicarchitect.breakbadhabits.infrastructure.DeepLinkChannel
import epicarchitect.breakbadhabits.monetization.PurchasePlatform
import epicarchitect.breakbadhabits.screens.root.RootScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        window.isNavigationBarContrastEnforced = false
        protectRecentAppsPreview()

        if (savedInstanceState == null) {
            PurchasePlatform.handlePayIntent(intent)
        }
        handleDeepLink(intent)

        setContent {
            RootScreen()
        }
    }

    private fun protectRecentAppsPreview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            setRecentsScreenshotEnabled(false)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        PurchasePlatform.handlePayIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val deepLinks = MainIntentDeepLinkParser.parse(intent)
        intent.removeExtra(EXTRA_HABIT_ID)
        intent.removeExtra(EXTRA_PAYWALL_SOURCE)
        deepLinks.forEach(DeepLinkChannel::trySend)
    }

    companion object {
        const val EXTRA_HABIT_ID = "habit_id"
        const val EXTRA_PAYWALL_SOURCE = "paywall_source"
    }
}
