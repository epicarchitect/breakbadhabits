package epicarchitect.breakbadhabits.settings

import kotlin.test.Test
import kotlin.test.assertEquals

class HabitOrderingTest {

    @Test
    fun usesSavedOrderAndKeepsUnknownItemsAtEnd() {
        assertEquals(
            listOf("c", "a", "b"),
            listOf("a", "b", "c").orderedBy(
                habitOrder = listOf("c", "a"),
                id = { it }
            )
        )
    }

    @Test
    fun emptyOrderKeepsOriginalOrder() {
        assertEquals(
            listOf("a", "b", "c"),
            listOf("a", "b", "c").orderedBy(
                habitOrder = emptyList(),
                id = { it }
            )
        )
    }
}
