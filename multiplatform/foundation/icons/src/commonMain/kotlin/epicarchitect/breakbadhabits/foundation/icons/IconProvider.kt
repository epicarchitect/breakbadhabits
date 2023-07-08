package epicarchitect.breakbadhabits.foundation.icons

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IconProvider {
    fun iconsFlow(): Flow<List<Icon>>
    fun iconFlow(id: Int): Flow<Icon?> = iconsFlow().map {
        it.firstOrNull { it.id == id }
    }
}