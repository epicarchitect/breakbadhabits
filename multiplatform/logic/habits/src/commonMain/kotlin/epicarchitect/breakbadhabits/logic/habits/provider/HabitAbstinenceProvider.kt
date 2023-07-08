package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.coroutines.flow.flatMapOrNullLatest
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.math.ranges.combineIntersections
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.withCurrentInstantAndTimeZone
import epicarchitect.breakbadhabits.logic.habits.model.HabitAbstinence
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.toLocalDateTime

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun currentAbstinenceFlow(
        habitId: Int
    ) = habitTrackProvider.habitTrackWithMaxEndFlow(habitId).flatMapOrNullLatest { lastTrack ->
        dateTimeProvider.withCurrentInstantAndTimeZone { instant, timeZone ->
            val range = lastTrack.dateTimeRange.endInclusive..instant.toLocalDateTime(timeZone)
            HabitAbstinence(
                habitId = habitId,
                dateTimeRange = range,
                duration = range.duration(timeZone)
            )
        }
    }.flowOn(coroutineDispatchers.default)

    fun abstinenceListFlow(
        habitId: Int
    ) = habitTrackProvider.habitTracksFlow(habitId).flatMapLatest { tracks ->
        val ranges = tracks.map(HabitTrack::dateTimeRange).combineIntersections()
        if (ranges.isEmpty()) {
            flowOf(emptyList())
        } else {
            dateTimeProvider.withCurrentInstantAndTimeZone { instant, timeZone ->
                List(ranges.size) { index ->
                    val range = if (index == ranges.lastIndex) {
                        ranges[index].endInclusive..instant.toLocalDateTime(timeZone)
                    } else {
                        ranges[index].endInclusive..ranges[index + 1].start
                    }
                    HabitAbstinence(
                        habitId = habitId,
                        dateTimeRange = range,
                        duration = range.duration(timeZone)
                    )
                }
            }
        }
    }
}