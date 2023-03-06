package breakbadhabits.app.logic.habits.serializer

import breakbadhabits.app.entity.HabitTrack

class HabitTrackSerializer {
    private val timeUnitMap = mapOf(
        HabitTrack.EventCount.TimeUnit.MINUTES to 0L,
        HabitTrack.EventCount.TimeUnit.HOURS to 1L,
        HabitTrack.EventCount.TimeUnit.DAYS to 2L
    )

    fun encodeEventCountTimeUnit(unit: HabitTrack.EventCount.TimeUnit) = timeUnitMap[unit]!!

    fun decodeEventCountTimeUnit(unit: Long) = timeUnitMap.keys.first { timeUnitMap[it] == unit }
}