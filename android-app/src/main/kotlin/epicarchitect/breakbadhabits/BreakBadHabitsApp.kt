package epicarchitect.breakbadhabits

import android.app.Application
import android.appwidget.AppWidgetManager
import epicarchitect.breakbadhabits.analytics.AppMetricaAnalyticsOperator
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.database.AndroidAppDatabaseFactory
import epicarchitect.breakbadhabits.database.HabitWidget
import epicarchitect.breakbadhabits.database.HabitWithLastEvent
import epicarchitect.breakbadhabits.datetime.format.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.export.AndroidDataExporter
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.AppDatabaseFactory
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import epicarchitect.breakbadhabits.infrastructure.DateTimeFormatter
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.PurchasePlatform
import epicarchitect.breakbadhabits.url.AndroidUrlLauncher
import epicarchitect.breakbadhabits.widget.updateHabitsAppWidgets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.time.Clock

private const val TargetStore = "target_store"

class BreakBadHabitsApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        isDebug = BuildConfig.DEBUG
        AppDatabaseFactory = AndroidAppDatabaseFactory(context = this)
        DateTimeFormatter = AndroidDateTimeFormatter(
            context = this
        )
        AnalyticsOperator = AppMetricaAnalyticsOperator(application = this)
        PaywallAnalytics.setPremium(isPremium = false)
        UrlOpener = AndroidUrlLauncher(context = this)
        Monetization = PurchasePlatform.create()
        DataExporter = AndroidDataExporter(
            context = this
        )
        appVersion = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
        AnalyticsOperator.setUserProperty(TargetStore, BuildConfig.TARGET_STORE)
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
