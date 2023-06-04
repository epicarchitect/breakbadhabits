package epicarchitect.breakbadhabits

import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import kotlinx.coroutines.flow.flowOf

class FakeIcon(override val id: Int) : Icon

class HabitIconProvider : IconProvider {
    private val icons = List(25) { FakeIcon(it) }

    override fun iconsFlow() = flowOf(icons)

    override suspend fun findIcon(id: Int) = icons.firstOrNull { it.id == id }
}