package epicarchitect.breakbadhabits.analytics

object IosAnalytics : AnalyticsOperator {
    override var isEnabled: Boolean
        get() = false
        set(value) = Unit

    override fun logEvent(name: String, parameters: Map<String, Any>) = Unit

    override fun setUserProperty(name: String, value: String?) = Unit

    override fun logError(throwable: Throwable) = Unit
}
