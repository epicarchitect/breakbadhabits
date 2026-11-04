package epicarchitect.breakbadhabits

import android.app.Activity
import android.app.Application
import android.appwidget.AppWidgetManager
import android.os.Bundle
import epicarchitect.breakbadhabits.analytics.AppMetricaAnalyticsOperator
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.database.AndroidAppDatabaseFactory
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.export.AndroidDataExporter
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.AppDatabaseFactory
import epicarchitect.breakbadhabits.infrastructure.Application
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.PurchasePlatform
import epicarchitect.breakbadhabits.widget.updateHabitsAppWidgets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.time.Clock

private const val TargetStore = "target_store"

class BreakBadHabitsApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var startedActivities = 0
    private var hasBackgrounded = false
    private val monetizationRefreshCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityStarted(activity: Activity) {
            if (startedActivities == 0 && hasBackgrounded && Monetization.purchasesAvailable) {
                applicationScope.launch {
                    runCatching { Monetization.restorePurchases() }
                }
            }
            startedActivities++
        }

        override fun onActivityStopped(activity: Activity) {
            if (startedActivities > 0) startedActivities--
            if (startedActivities == 0) hasBackgrounded = true
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
        override fun onActivityResumed(activity: Activity) = Unit
        override fun onActivityPaused(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
    }

    override fun onCreate() {
        super.onCreate()
        Application = this
        isDebug = BuildConfig.DEBUG
        AppDatabaseFactory = AndroidAppDatabaseFactory(context = this)
        AnalyticsOperator = AppMetricaAnalyticsOperator(application = this)
        PaywallAnalytics.setPremium(isPremium = false)
        Monetization = PurchasePlatform.create()
        DataExporter = AndroidDataExporter(
            context = this
        )
        appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        AnalyticsOperator.setUserProperty(TargetStore, BuildConfig.TARGET_STORE)
        registerActivityLifecycleCallbacks(monetizationRefreshCallbacks)
        observeHabitWidgets()
    }

    private fun observeHabitWidgets() {
        applicationScope.launch {
            combine(
                AppDatabase.habitWidgetDao.habitWidgets(),
                AppDatabase.habitsDao.habitsWithLastEvent(),
                Monetization.premiumCache
            ) { widgets, habits, premiumCache ->
                HabitWidgetsState(
                    widgets = widgets,
                    habits = habits,
                    isPremium = premiumCache?.expiresAt
                        ?.let { it > Clock.System.now() } == true
                )
            }.collect { state ->
                updateHabitsAppWidgets(
                    context = this@BreakBadHabitsApp,
                    manager = AppWidgetManager.getInstance(this@BreakBadHabitsApp),
                    widgets = state.widgets,
                    habitsWithLastEvent = state.habits,
                    isPremium = state.isPremium
                )
            }
        }
    }
}

private data class HabitWidgetsState(
    val widgets: List<HabitWidget>,
    val habits: List<HabitWithLastEvent>,
    val isPremium: Boolean
)
