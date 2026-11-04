package epicarchitect.breakbadhabits.habits

import kotlin.test.Test
import kotlin.test.assertEquals

class HabitIconsTest {

    @Test
    fun idsAreUnique() {
        assertEquals(
            HabitIcons.items.size,
            HabitIcons.items.map { it.id }.toSet().size
        )
    }

    @Test
    fun knownIdReturnsMatchingIcon() {
        assertEquals(0, HabitIcons.getById(0).id)
        assertEquals(1, HabitIcons.getById(1).id)
        assertEquals(HabitIcons.items.last().id, HabitIcons.getById(HabitIcons.items.last().id).id)
    }

    @Test
    fun unknownIdFallsBackToFirstIcon() {
        assertEquals(HabitIcons.items.first().id, HabitIcons.getById(-1).id)
        assertEquals(HabitIcons.items.first().id, HabitIcons.getById(999).id)
    }
}
