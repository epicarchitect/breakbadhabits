package breakbadhabits.android.app.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService
import breakbadhabits.android.app.formatter.AbstinenceTimeFormatter
import org.koin.android.ext.android.get

class HabitsAppWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = HabitsAppWidgetRemoteViewsFactory(
        applicationContext,
        habitsRepository = get(),
        appWidgetsRepository = get(),
        AbstinenceTimeFormatter(applicationContext),
        intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    )
}
