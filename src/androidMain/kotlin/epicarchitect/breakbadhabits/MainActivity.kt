package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import epicarchitect.breakbadhabits.habits.widget.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.ui.Root

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Root()
        }
    }

    override fun onPause() {
        super.onPause()
        HabitsAppWidgetProvider.sendUpdateIntent(this)
    }
}