package epicarchitect.breakbadhabits.entity.util

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun <T : Any> Query<T>.flowOfList() = asFlow().mapToList(Dispatchers.IO)
fun <T : Any> Query<T>.flowOfOneOrNull() = asFlow().mapToOneOrNull(Dispatchers.IO)