package breakbadhabits.android.app.appwidget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.RemoteViewsService
import breakbadhabits.android.app.appDependencies

class HabitsAppWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = HabitsAppWidgetRemoteViewsFactory(
        context = applicationContext,
        habitsRepository = appDependencies.habitsRepository,
        appWidgetsRepository = appDependencies.appWidgetsRepository,
        abstinenceTimeFormatter = appDependencies.abstinenceTimeFormatter,
        appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    )
}
