package epicarchitect.breakbadhabits.foundation.sqldelight

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.coroutines.flow.mapItems
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

fun <T : Any, R : Any> Query<T>.asFlowOfList(
    coroutineDispatchers: CoroutineDispatchers,
    transform: suspend (T) -> R
) = asFlow()
    .mapToList(coroutineDispatchers.io)
    .mapItems(transform)
    .flowOn(coroutineDispatchers.default)

fun <T : Any, R : Any> Query<T>.asFlowOfOneOrNull(
    coroutineDispatchers: CoroutineDispatchers,
    transform: suspend (T) -> R
) = asFlow()
    .mapToOneOrNull(coroutineDispatchers.io)
    .map {
        if (it == null) {
            null
        } else {
            transform(it)
        }
    }
    .flowOn(coroutineDispatchers.default)