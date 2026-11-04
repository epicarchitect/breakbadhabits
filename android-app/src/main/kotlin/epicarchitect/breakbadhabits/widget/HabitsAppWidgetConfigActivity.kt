package epicarchitect.breakbadhabits.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.screens.habits.widgets.creation.HabitWidgetCreationRoot

class HabitsAppWidgetConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.App_Theme)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        window.isNavigationBarContrastEnforced = false
        setResult(Activity.RESULT_CANCELED)

        val systemWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )

        setContent {
            HabitWidgetCreationRoot(
                systemWidgetId = systemWidgetId,
                onDone = {
                    setResult(
                        Activity.RESULT_OK,
                        Intent().putExtra(
                            AppWidgetManager.EXTRA_APPWIDGET_ID,
                            systemWidgetId
                        )
                    )
                    finish()
                    HabitsAppWidgetProvider.sendUpdateBroadcast(this)
                }
            )
        }
    }
}
