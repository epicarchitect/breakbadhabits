package breakbadhabits.android.app.activity

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import breakbadhabits.android.app.App
import breakbadhabits.android.app.R
import breakbadhabits.android.app.compose.screen.HabitsAppWidgetConfigCreationScreen
import breakbadhabits.android.app.utils.NightModeManager
import breakbadhabits.compose.theme.BreakBadHabitsTheme

class HabitsAppWidgetConfigCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Activity)
        val appWidgetId = intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
        setContent {
            BreakBadHabitsTheme(
                isDarkTheme = when (App.architecture.nightModeManager.mode) {
                    NightModeManager.Mode.NIGHT -> true
                    NightModeManager.Mode.NOT_NIGHT -> false
                    NightModeManager.Mode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                HabitsAppWidgetConfigCreationScreen(
                    appWidgetId = appWidgetId,
                    onFinished = {
                        setResult(RESULT_OK, Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId))
                        finish()
                    }
                )
            }
        }
    }
}