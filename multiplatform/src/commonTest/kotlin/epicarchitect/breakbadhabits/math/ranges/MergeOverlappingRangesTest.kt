package epicarchitect.breakbadhabits.math.ranges

import kotlin.test.Test
import kotlin.test.assertEquals

class MergeOverlappingRangesTest {

    @Test
    fun emptyInput() {
        assertEquals(
            emptyList(),
            emptyList<IntRange>().mergedByOverlappingRanges()
        )
    }

    @Test
    fun singleRange() {
        assertEquals(
            listOf(1..3),
            listOf(1..3).mergedByOverlappingRanges()
        )
    }

    @Test
    fun mergesOverlappingRanges() {
        assertEquals(
            listOf(1 to 6, 7 to 10),
            listOf(1..3, 3..6, 8..10, 7..9).mergedByOverlappingRanges().bounds()
        )
    }

    @Test
    fun keepsAdjacentRangesSeparate() {
        assertEquals(
            listOf(1 to 3, 4 to 6),
            listOf(1..3, 4..6).mergedByOverlappingRanges().bounds()
        )
    }

    @Test
    fun keepsSeparatedRanges() {
        assertEquals(
            listOf(1 to 2, 5 to 6),
            listOf(1..2, 5..6).mergedByOverlappingRanges().bounds()
        )
    }

    @Test
    fun handlesNestedRanges() {
        assertEquals(
            listOf(1 to 10),
            listOf(1..10, 2..3, 4..8).mergedByOverlappingRanges().bounds()
        )
    }

    @Test
    fun handlesDescendingRanges() {
        assertEquals(
            listOf(1 to 6),
            listOf(6..1, 3..2).mergedByOverlappingRanges().bounds()
        )
    }

    private fun List<ClosedRange<Int>>.bounds() = map { it.start to it.endInclusive }
}
