package epicarchitect.breakbadhabits.habits.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService

class HabitsAppWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = HabitsAppWidgetRemoteViewsFactory(
        context = applicationContext,
        widgetSystemId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    )
}