package epicarchitect.breakbadhabits.foundation.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun <T> Flow<T?>.firstNotNull(): T = filterNotNull().first()

fun <T, R> Flow<List<T>>.mapItems(transform: suspend (T) -> R) = map {
    it.map { transform(it) }
}