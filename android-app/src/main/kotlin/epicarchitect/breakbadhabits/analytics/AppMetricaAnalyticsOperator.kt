package epicarchitect.breakbadhabits.analytics

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import epicarchitect.breakbadhabits.BuildConfig
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import io.appmetrica.analytics.profile.Attribute
import io.appmetrica.analytics.profile.UserProfile

class AppMetricaAnalyticsOperator(application: Application) : AnalyticsOperator {
    private val preferences = application.getSharedPreferences(
        "privacy_analytics",
        Context.MODE_PRIVATE
    )

    override var isEnabled: Boolean
        get() = preferences.getLong("consent_time", 0L) > 0L
        set(value) {
            preferences.edit(commit = true) {
                if (value) {
                    putLong("consent_time", System.currentTimeMillis())
                } else {
                    remove("consent_time")
                }
            }
            AppMetrica.setDataSendingEnabled(value)
        }

    init {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APPMETRICA_API_KEY)
            .withDataSendingEnabled(isEnabled)
            .withCrashReporting(true)
            .withNativeCrashReporting(false)
            .withAnrMonitoring(true)
            .withLocationTracking(false)
            .withAdvIdentifiersTracking(false)
            .build()

        AppMetrica.activate(application, config)
        AppMetrica.enableActivityAutoTracking(application)
        AppMetrica.setLocationTracking(false)
        AppMetrica.setAdvIdentifiersTracking(false)
    }

    override fun logEvent(name: String, parameters: Map<String, Any>) {
        AppMetrica.reportEvent(name, parameters)
    }

    override fun setUserProperty(name: String, value: String?) {
        val attribute = if (value == null) {
            Attribute.customString(name).withValueReset()
        } else {
            Attribute.customString(name).withValue(value)
        }
        AppMetrica.reportUserProfile(
            UserProfile.newBuilder()
                .apply(attribute)
                .build()
        )
    }

    override fun logError(throwable: Throwable) {
        AppMetrica.reportError("app_error", throwable)
    }
}
