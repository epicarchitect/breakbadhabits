package breakbadhabits.android.app.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService
import breakbadhabits.app.entity.HabitAppWidgetConfig

class HabitsAppWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = HabitsAppWidgetRemoteViewsFactory(
        context = applicationContext,
        appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ).let {
            HabitAppWidgetConfig.AppWidgetId(it.toLong())
        }
    )
}
