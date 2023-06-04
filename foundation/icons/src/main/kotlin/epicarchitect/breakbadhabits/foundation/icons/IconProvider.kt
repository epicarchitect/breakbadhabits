package epicarchitect.breakbadhabits.foundation.icons

import kotlinx.coroutines.flow.Flow

interface IconProvider {
    fun iconsFlow(): Flow<List<Icon>>
    suspend fun findIcon(id: Int): Icon?
}

suspend fun IconProvider.requireIcon(id: Int) = checkNotNull(findIcon(id))