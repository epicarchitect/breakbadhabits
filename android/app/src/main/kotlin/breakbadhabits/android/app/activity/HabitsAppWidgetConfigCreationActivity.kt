package breakbadhabits.android.app.activity

import android.appwidget.AppWidgetManager
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import breakbadhabits.android.app.R
import breakbadhabits.android.app.compose.screen.HabitsAppWidgetConfigCreationScreen
import breakbadhabits.android.compose.activity.ComposeActivity
import epicarchitect.epicstore.compose.RootEpicStore


class HabitsAppWidgetConfigCreationActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        RootEpicStore {
            val appWidgetId = remember {
                intent.extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
            }

            HabitsAppWidgetConfigCreationScreen(
                appWidgetId = appWidgetId,
                onFinished = {
                    setResult(
                        RESULT_OK,
                        Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    )
                    finish()
                }
            )
        }
    }
}