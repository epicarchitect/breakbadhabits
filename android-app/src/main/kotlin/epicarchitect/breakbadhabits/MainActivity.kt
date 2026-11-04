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
import epicarchitect.breakbadhabits.monetization.PurchasePlatform
import epicarchitect.breakbadhabits.screens.root.RootScreen
import epicarchitect.breakbadhabits.widget.HabitsAppWidgetProvider

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
        PurchasePlatform.handlePayIntent(intent)
    }

    override fun onStop() {
        super.onStop()
        HabitsAppWidgetProvider.sendUpdateBroadcast(this)
    }
}
