package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.ascended
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {
    fun currentAbstinenceFlow(
        habitId: Habit.Id
    ) = habitTrackProvider.habitTrackFlowByMaxEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTimeFlow().map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(lastTrack.time.endInclusive..currentTime)
            )
        }
    }.flowOn(Dispatchers.Default)

    fun abstinenceListFlow(habitId: Habit.Id) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTimeFlow(),
    ) { tracks, currentTime ->
        tracks.map { it.time }.normalized().let { sortedList ->
            List(sortedList.size) { index ->
                HabitAbstinence(
                    habitId = habitId,
                    range = HabitAbstinence.Range(
                        if (index == sortedList.lastIndex) {
                            sortedList[index].endInclusive..currentTime
                        } else {
                            sortedList[index].endInclusive..sortedList[index + 1].start
                        }
                    )
                )
            }
        }
    }.flowOn(Dispatchers.Default)
}

private fun <T : Comparable<T>> ClosedRange<T>.valuesAreEquals(
    range: ClosedRange<T>
) = start == range.start && endInclusive == range.endInclusive

private fun <T : Comparable<T>> List<ClosedRange<T>>.normalized(): List<ClosedRange<T>> {
    if (size < 2) return this

    val mutableList = toSet().sortedBy(ClosedRange<T>::start).toMutableList()

    var isNormalized = false
    while (isNormalized.not()) {
        isNormalized = true
        val checkingItem = mutableList[0]
        var checkingItem2Index = 1
        while (
            checkingItem2Index < mutableList.size && mutableList.find {
                it.valuesAreEquals(checkingItem)
            } != null
        ) {
            val checkingItem2 = mutableList[checkingItem2Index]

            if (checkingItem.valuesAreEquals(checkingItem2)) {
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(0, checkingItem2)
                isNormalized = false
                break
            }

            val result = combineOrNull(checkingItem, checkingItem2)

            if (result != null) {
                mutableList.removeAll { it.valuesAreEquals(checkingItem) }
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(0, result)
                checkingItem2Index = 1
                isNormalized = false
            } else {
                checkingItem2Index++
            }
        }
    }

    return mutableList.toList()
}

operator fun <T : Comparable<T>> ClosedRange<T>.contains(
    range: ClosedRange<T>
): Boolean = start in range || endInclusive in range

fun <T : Comparable<T>> combineOrNull(
    range1: ClosedRange<T>,
    range2: ClosedRange<T>,
): ClosedRange<T>? {
    val r1 = range1.ascended()
    val r2 = range2.ascended()
    return when {
        r1.start == r2.start && r1.endInclusive == r2.endInclusive -> r1
        r1 in r2 || r2 in r1 -> listOf(
            r1.start,
            r1.endInclusive,
            r2.start,
            r2.endInclusive
        ).let {
            it.min()..it.max()
        }
        else -> null
    }
}
