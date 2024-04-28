package epicarchitect.breakbadhabits.di.declaration

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers

interface CoroutinesModule {
    val coroutineDispatchers: CoroutineDispatchers
}