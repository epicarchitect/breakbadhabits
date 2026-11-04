package epicarchitect.breakbadhabits.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import epicarchitect.breakbadhabits.MainActivity
import epicarchitect.breakbadhabits.R
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitsAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        manager: AppWidgetManager,
        widgetSystemIds: IntArray
    ) {
        super.onUpdate(context, manager, widgetSystemIds)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                widgetSystemIds.forEach { widgetSystemId ->
                    runCatching {
                        updateAppWidget(context, manager, widgetSystemId)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        widgetSystemId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(
            context,
            appWidgetManager,
            widgetSystemId,
            newOptions
        )
        sendUpdateBroadcast(context)
    }

    override fun onDeleted(context: Context, widgetSystemIds: IntArray) {
        super.onDeleted(context, widgetSystemIds)

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                widgetSystemIds.forEach { widgetSystemId ->
                    runCatching {
                        AppDatabase.habitWidgetDao.deleteBySystemId(widgetSystemId)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun updateAppWidget(
        context: Context,
        manager: AppWidgetManager,
        widgetSystemId: Int
    ) {
        val widget = AppDatabase.habitWidgetDao.widgetBySystemId(widgetSystemId)
        val layoutId = if (context.isSystemDarkModeEnabled()) {
            R.layout.habits_app_widget_dark
        } else {
            R.layout.habits_app_widget_light
        }

        if (widget == null) {
            manager.updateAppWidget(
                widgetSystemId,
                RemoteViews(context.packageName, layoutId).apply {
                    setTextViewText(
                        R.id.title_textView,
                        context.getString(R.string.widget_not_configured)
                    )
                    setViewVisibility(R.id.title_textView, View.VISIBLE)
                    setOnClickPendingIntent(R.id.widget_root, mainActivityPendingIntent(context))
                }
            )
            return
        }

        val items = loadHabitsAppWidgetItems(widget)
        val views = RemoteViews(context.packageName, layoutId).apply {
            setTextViewText(R.id.title_textView, widget.title)
            setViewVisibility(
                R.id.title_textView,
                if (widget.title.isEmpty()) View.GONE else View.VISIBLE
            )
            setRemoteAdapter(
                R.id.habits_listView,
                buildRemoteCollectionItems(context, items)
            )
            setOnClickPendingIntent(R.id.widget_root, mainActivityPendingIntent(context))
        }

        manager.updateAppWidget(widgetSystemId, views)
    }

    companion object {
        fun sendUpdateBroadcast(context: Context) {
            val component = ComponentName(context, HabitsAppWidgetProvider::class.java)
            val widgetSystemIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(component)
            if (widgetSystemIds.isEmpty()) return

            context.sendBroadcast(
                Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                    this.component = component
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetSystemIds)
                }
            )
        }
    }
}

private fun mainActivityPendingIntent(context: Context): PendingIntent =
    PendingIntent.getActivity(
        context,
        0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

private fun buildRemoteCollectionItems(
    context: Context,
    items: List<HabitsAppWidgetItem>
) = RemoteViews.RemoteCollectionItems.Builder()
    .setHasStableIds(true)
    .setViewTypeCount(1)
    .apply {
        items.forEach { item ->
            addItem(
                item.habit.id.hashCode().toLong(),
                createHabitsAppWidgetItemView(context, item)
            )
        }
    }
    .build()

private fun Context.isSystemDarkModeEnabled() =
    (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES
