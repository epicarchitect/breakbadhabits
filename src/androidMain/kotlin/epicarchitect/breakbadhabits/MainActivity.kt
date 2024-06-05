package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.compose.setContent
import epicarchitect.breakbadhabits.habits.widget.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.ui.screen.Root

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Root()
        }
    }

    override fun onPause() {
        super.onPause()
        HabitsAppWidgetProvider.sendUpdateBroadcast(this)
    }
}