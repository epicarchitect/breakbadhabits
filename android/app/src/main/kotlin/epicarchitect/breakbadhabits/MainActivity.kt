package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import epicarchitect.breakbadhabits.habits.widget.android.HabitsAppWidgetProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onPause() {
        super.onPause()
        HabitsAppWidgetProvider.sendUpdateIntent(this)
    }
}