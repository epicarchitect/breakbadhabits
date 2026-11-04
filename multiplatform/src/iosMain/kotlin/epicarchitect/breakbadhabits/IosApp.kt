package epicarchitect.breakbadhabits

import androidx.compose.ui.window.ComposeUIViewController
import epicarchitect.breakbadhabits.analytics.IosAnalytics
import epicarchitect.breakbadhabits.analytics.PaywallAnalytics
import epicarchitect.breakbadhabits.database.IosAppDatabaseFactory
import epicarchitect.breakbadhabits.datetime.format.IosDateTimeFormatter
import epicarchitect.breakbadhabits.export.IosDataExporter
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabaseFactory
import epicarchitect.breakbadhabits.infrastructure.DataExporter
import epicarchitect.breakbadhabits.infrastructure.DateTimeFormatter
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.infrastructure.UrlOpener
import epicarchitect.breakbadhabits.infrastructure.appVersion
import epicarchitect.breakbadhabits.infrastructure.isDebug
import epicarchitect.breakbadhabits.monetization.IosMonetizationEnvironment
import epicarchitect.breakbadhabits.screens.root.RootScreen
import epicarchitect.breakbadhabits.url.IosUrlLauncher
import platform.Foundation.NSBundle
import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@Suppress("unused")
class IosAppInitializer {
    fun initialize() {
        isDebug = Platform.isDebugBinary
        AppDatabaseFactory = IosAppDatabaseFactory()
        DateTimeFormatter = IosDateTimeFormatter()
        AnalyticsOperator = IosAnalytics
        PaywallAnalytics.setPremium(isPremium = false)
        UrlOpener = IosUrlLauncher()
        Monetization = IosMonetizationEnvironment()
        DataExporter = IosDataExporter()
        val version = NSBundle.mainBundle
            .objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: ""
        val build = NSBundle.mainBundle
            .objectForInfoDictionaryKey("CFBundleVersion") as? String ?: ""
        appVersion = if (version.isBlank() && build.isBlank()) "" else "$version ($build)"
    }
}

@Suppress("unused")
fun createAppViewController(): UIViewController = ComposeUIViewController {
    RootScreen()
}
