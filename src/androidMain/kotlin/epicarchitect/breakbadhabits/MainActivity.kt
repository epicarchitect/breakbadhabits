package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import epicarchitect.breakbadhabits.habits.widget.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.ui.screen.app.App

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.App_Theme)
        setContent {
            App()
        }
    }

    override fun onPause() {
        super.onPause()
        HabitsAppWidgetProvider.sendUpdateBroadcast(this)
    }
}