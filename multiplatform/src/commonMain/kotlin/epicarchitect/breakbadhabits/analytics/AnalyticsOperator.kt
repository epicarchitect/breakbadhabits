package epicarchitect.breakbadhabits.analytics

interface AnalyticsOperator {
    var isEnabled: Boolean

    fun logEvent(name: String, parameters: Map<String, Any> = emptyMap())

    fun setUserProperty(name: String, value: String?)

    fun logError(throwable: Throwable)
}
