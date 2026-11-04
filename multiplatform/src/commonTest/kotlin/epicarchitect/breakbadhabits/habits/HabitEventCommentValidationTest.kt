package epicarchitect.breakbadhabits.habits

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HabitEventCommentValidationTest {

    @Test
    fun emptyCommentIsValid() {
        assertNull(checkHabitEventComment("", maxLength = 10))
    }

    @Test
    fun commentAtLimitIsValid() {
        assertNull(checkHabitEventComment("1234567890", maxLength = 10))
    }

    @Test
    fun commentOverLimitIsRejected() {
        assertEquals(
            HabitEventCommentError.TooLong(10),
            checkHabitEventComment("12345678901", maxLength = 10)
        )
    }

    @Test
    fun hugeCommentIsNotKeptInStateAndLimitedValueIsValid() {
        val hugeComment = "x".repeat(10_000_000)
        val maxLength = 1_000
        val limitedComment = limitHabitEventComment(hugeComment, maxLength)

        assertEquals(maxLength, limitedComment.length)
        assertNull(checkHabitEventComment(limitedComment, maxLength))
    }

    @Test
    fun sourceValueOverLimitStillReportsTooLong() {
        assertEquals(
            HabitEventCommentError.TooLong(1_000),
            checkHabitEventComment("x".repeat(1_001), 1_000)
        )
    }
}
