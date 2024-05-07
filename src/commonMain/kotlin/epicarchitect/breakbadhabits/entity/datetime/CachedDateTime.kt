package epicarchitect.breakbadhabits.entity.datetime

class CachedDateTime(
    private val base: DateTime
) : DateTime {
    private val instant by lazy(base::instant)
    private val timeZone by lazy(base::timeZone)
    private val local by lazy(base::local)

    override fun instant() = instant
    override fun timeZone() = timeZone
    override fun local() = local
}