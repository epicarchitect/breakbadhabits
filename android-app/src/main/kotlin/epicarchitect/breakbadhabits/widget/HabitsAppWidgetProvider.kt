package epicarchitect.breakbadhabits.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import epicarchitect.breakbadhabits.MainActivity
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Clock

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
                updateHabitsAppWidgets(
                    context = context,
                    manager = manager,
                    widgets = AppDatabase.habitWidgetDao.habitWidgets().first(),
                    habitsWithLastEvent = AppDatabase.habitsDao.habitsWithLastEvent().first(),
                    isPremium = Monetization.premiumCache.value?.expiresAt
                        ?.let { it > Clock.System.now() } == true,
                    widgetSystemIds = widgetSystemIds
                )
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

    companion object {
        fun sendUpdateBroadcast(context: Context) {
            val component = ComponentName(context, HabitsAppWidgetProvider::class.java)
            val widgetSystemIds = AppWidgetManager.getInstance(context)
                .getAppWidgetIds(component)
            if (widgetSystemIds.isNotEmpty()) {
                context.sendBroadcast(
                    Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE).apply {
                        this.component = component
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetSystemIds)
                    }
                )
            }
        }
    }
}

internal fun mainActivityPendingIntent(
    context: Context,
    requestCode: Int = 0,
    mutable: Boolean = false,
    paywallSource: String? = null
): PendingIntent = PendingIntent.getActivity(
    context,
    requestCode,
    Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        if (paywallSource != null) {
            putExtra(MainActivity.EXTRA_PAYWALL_SOURCE, paywallSource)
        }
    },
    (if (mutable) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_IMMUTABLE) or
            PendingIntent.FLAG_UPDATE_CURRENT
)
