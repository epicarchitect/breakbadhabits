package epicarchitect.breakbadhabits.newarch.time

class CachedAppTime(
    private val base: AppTime
) : AppTime {
    private val instant by lazy(base::instant)
    private val timeZone by lazy(base::timeZone)

    override fun instant() = instant
    override fun timeZone() = timeZone
}