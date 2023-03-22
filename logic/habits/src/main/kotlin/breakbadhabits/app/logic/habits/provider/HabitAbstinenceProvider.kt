package breakbadhabits.app.logic.habits.provider

import android.util.Log
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.ascended
import breakbadhabits.foundation.datetime.secondsToInstantRange
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
            }.also {
                Log.d("test123", "2 =========")
                val result = it.map {
                    it.range.start.epochSeconds..it.range.endInclusive.epochSeconds
                }
                Log.d("test123", result.toString())
                Log.d("test123", "2 =========")
            }
        }
    }.flowOn(Dispatchers.Default)
}

private fun <T : Comparable<T>> ClosedRange<T>.valuesAreEquals(
    range: ClosedRange<T>
) = start == range.start && endInclusive == range.endInclusive

private fun print(text: String) {
    println(text)
}

private fun <T : Comparable<T>> List<ClosedRange<T>>.normalized(): List<ClosedRange<T>> {
    if (size < 2) return this

    val mutableList = toSet().sortedBy(ClosedRange<T>::start).toMutableList()

    print("1 mutableList: $mutableList")

    var checkingItemIndex = 0
    var isNormalized = false
    while (isNormalized.not()) {
        print("new while 1")
        isNormalized = true
        val checkingItem = mutableList[checkingItemIndex]
        print("2 checkingItem: $checkingItem, isNormalized: $isNormalized")
        var checkingItem2Index = 0
        while (
            checkingItem2Index < mutableList.size && mutableList.find {
                it.valuesAreEquals(checkingItem)
            } != null
        ) {
            print("new while 2")
            if (checkingItemIndex == checkingItem2Index) {
                print("checkingItemIndex == checkingItem2Index, do checkingItem2Index++")
                checkingItem2Index++
                print("checkingItem2Index: $checkingItem2Index")
                print("continue")
                continue
            }

            val checkingItem2 = mutableList[checkingItem2Index]
            print("3 checkingItem2: $checkingItem2, index: $checkingItem2Index")
            print("4 mutableList: $mutableList, isNormalized: $isNormalized")

            if (checkingItem.valuesAreEquals(checkingItem2)) {
                print("5 $checkingItem.valuesAreEquals($checkingItem2) == true")
                print("6 mutableList: $mutableList, isNormalized: $isNormalized, do remove all")
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(checkingItemIndex, checkingItem2)
                isNormalized = false
                print("7 mutableList: $mutableList, isNormalized: $isNormalized, new list")
                break
            }

            val result = combineOrNull(checkingItem, checkingItem2)
            print("8 result = combineOrNull($checkingItem, $checkingItem2), result: $result")

            if (result != null) {
                print("9 mutableList: $mutableList, isNormalized: $isNormalized, do remove item1, item2 and insert result")
                mutableList.removeAll { it.valuesAreEquals(checkingItem) }
                mutableList.removeAll { it.valuesAreEquals(checkingItem2) }
                mutableList.add(0, result)
                checkingItem2Index = 0
                isNormalized = false
                print("10 mutableList: $mutableList, isNormalized: $isNormalized, new list after result")
            } else {
                checkingItem2Index++
                print("11 result == null mutableList: $mutableList, isNormalized: $isNormalized, list not change")
                print("12 checkingItem2Index: $checkingItem2Index")
            }
            print("end while 2")
        }
        print("13 isNormalized: $isNormalized")
        if (isNormalized) {
            if (checkingItemIndex < mutableList.size - 1) {
                isNormalized = false
                print("15 checkingItemIndex($checkingItemIndex) < mutableList.size(${mutableList.size}) == true: set isNormalized = false")
            }
            checkingItemIndex++
        } else {
            checkingItemIndex = 0
        }
        print("14 checkingItemIndex: $checkingItemIndex")
        print("end while 1")
    }

    return mutableList.map(ClosedRange<T>::ascended).sortedBy(ClosedRange<T>::start)
}

fun main() {
    val list = listOf(
        16774884..16779204,
        16790400..16790400,
        16790400..16790436,
        16792128..16792128,
        16793856..16793856
    )
//
//    println(list)
//    println(list.normalized())

    println(
        listOf(
            1..3,
            4..4,
            4..7,
            8..8,
            10..10
        ).normalized()
    )
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
