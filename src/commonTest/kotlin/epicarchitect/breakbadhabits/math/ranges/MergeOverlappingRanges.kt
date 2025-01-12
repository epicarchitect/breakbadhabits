package epicarchitect.breakbadhabits.math.ranges

import kotlin.test.Test
import kotlin.test.assertContentEquals

class MergeOverlappingRangesTest {
    @Test
    fun `simple data test`() {
        val data = listOf(1..3, 3..6, 8..10, 7..9)
        val expected = listOf(1..6, 7..10)
        val actual = data.mergedByOverlappingRanges().map { it.start..it.endInclusive }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `strange data test`() {
        val data = listOf(3..1, 6..3, 10..8, 9..7)
        val expected = listOf(1..6, 7..10)
        val actual = data.mergedByOverlappingRanges().map { it.start..it.endInclusive }
        assertContentEquals(expected, actual)
    }

    @Test
    fun `empty data test`() {
        val data = emptyList<ClosedRange<Int>>()
        val expected = emptyList<ClosedRange<Int>>()
        val actual = data.mergedByOverlappingRanges().map { it.start..it.endInclusive }
        assertContentEquals(expected, actual)
    }
}