package epicarchitect.breakbadhabits.habits.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.screens.habits.widgets.creation.HabitWidgetCreation
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

class HabitsAppWidgetConfigCreationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.App_Theme)
        val systemWidgetId = intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
        setContent {
            FlowStateContainer(
                state = stateOfOneOrNull { Environment.database.appSettingsQueries.settings() }
            ) { settings ->
                AppTheme(
                    colorScheme = AppColorsSchemes.byAppSettings(settings!!)
                ) {
                    HabitWidgetCreation(
                        systemWidgetId = systemWidgetId,
                        onDone = {
                            setResult(
                                RESULT_OK,
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
    }
}