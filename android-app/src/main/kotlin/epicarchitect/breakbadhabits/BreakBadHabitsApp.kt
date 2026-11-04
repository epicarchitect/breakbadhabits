package epicarchitect.breakbadhabits

import android.app.Application
import epicarchitect.breakbadhabits.analytics.AppMetricaAnalyticsOperator
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.database.AndroidAppDatabaseFactory
import epicarchitect.breakbadhabits.datetime.format.AndroidDateTimeFormatter
import epicarchitect.breakbadhabits.export.AndroidDataExporter
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabaseFactory
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import epicarchitect.breakbadhabits.infrastructure.DateTimeFormatter
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.PurchasePlatform
import epicarchitect.breakbadhabits.url.AndroidUrlLauncher

private const val TargetStore = "target_store"

class BreakBadHabitsApp : Application() {
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
    }
}
