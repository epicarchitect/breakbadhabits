package epicarchitect.breakbadhabits.habits.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.BaseActivity
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.screen.habits.widgets.creation.HabitWidgetCreation

class HabitsAppWidgetConfigCreationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val systemWidgetId = intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
        setContent {
            val appSettingsState = remember {
                AppData.database.appSettingsQueries.settings().flowOfOneOrNull()
            }.collectAsState(initial = null)

            val appSettings = appSettingsState.value ?: return@setContent

            AppTheme(
                colorScheme = AppColorsSchemes.byAppSettings(appSettings)
            ) {
                HabitWidgetCreation(
                    systemWidgetId = systemWidgetId,
                    onDone = {
                        setResult(
                            RESULT_OK,
                            Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, systemWidgetId)
                        )
                        finish()
                        HabitsAppWidgetProvider.sendUpdateBroadcast(this)
                    }
                )
            }
        }
    }
}