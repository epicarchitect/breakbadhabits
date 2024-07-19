package epicarchitect.breakbadhabits

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import epicarchitect.breakbadhabits.habits.widget.HabitsAppWidgetProvider
import epicarchitect.breakbadhabits.screens.app.App

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }

    override fun onPause() {
        super.onPause()
        HabitsAppWidgetProvider.sendUpdateBroadcast(this)
    }
}