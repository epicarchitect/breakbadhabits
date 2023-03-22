package breakbadhabits.android.app.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import breakbadhabits.android.app.BreakBadHabitsApp
import breakbadhabits.android.app.R
import breakbadhabits.app.entity.HabitAppWidgetConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HabitsAppWidgetProvider : AppWidgetProvider() {

    private val logicModule by lazy {
        BreakBadHabitsApp.instance.logicModule
    }

    override fun onUpdate(context: Context, manager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, manager, appWidgetIds)
        appWidgetIds.forEach { appWidgetId ->
            updateAppWidget(context, manager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        sendUpdateIntent(context)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) = runBlocking {
        super.onDeleted(context, appWidgetIds)
        appWidgetIds.forEach {
            logicModule.habitAppWidgetConfigDeleter.deleteByAppWidgetId(
                HabitAppWidgetConfig.AppWidgetId(it.toLong())
            )
        }
    }

    private fun updateAppWidget(
        context: Context,
        manager: AppWidgetManager,
        appWidgetId: Int
    ) = runBlocking {
        val isDarkModeEnabled =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        val config = logicModule.habitAppWidgetConfigProvider.provideFlowByAppWidgetId(
            HabitAppWidgetConfig.AppWidgetId(appWidgetId.toLong())
        ).first() ?: let {
            manager.updateAppWidget(
                appWidgetId,
                RemoteViews(
                    context.packageName,
                    R.layout.habits_app_widget_light
                ).apply {
                    setTextViewText(R.id.title_textView, "Not found")
                    setViewVisibility(R.id.title_textView, View.VISIBLE)
//                    setViewVisibility(R.id.divider, View.GONE)
                }
            )
            manager.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.habits_listView
            )
            return@runBlocking
        }

        val views = RemoteViews(
            context.packageName,
            if (isDarkModeEnabled)
                R.layout.habits_app_widget_dark
            else
                R.layout.habits_app_widget_light
        ).apply {
            setTextViewText(R.id.title_textView, config.title.value)
//            setViewVisibility(R.id.divider, if (config.title.value.isEmpty()) View.GONE else View.VISIBLE)
            setViewVisibility(
                R.id.title_textView,
                if (config.title.value.isEmpty()) View.GONE else View.VISIBLE
            )
            setRemoteAdapter(
                R.id.habits_listView,
                Intent(context, HabitsAppWidgetRemoteViewsService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                }
            )
        }

        manager.updateAppWidget(appWidgetId, views)
        manager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.habits_listView)
    }

    companion object {
        fun sendUpdateIntent(context: Context) = context.sendBroadcast(
            Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                component = ComponentName(context, HabitsAppWidgetProvider::class.java)
                putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_IDS,
                    AppWidgetManager.getInstance(context).getAppWidgetIds(
                        ComponentName(
                            context,
                            HabitsAppWidgetProvider::class.java
                        )
                    )
                )
            }
        )
    }
}