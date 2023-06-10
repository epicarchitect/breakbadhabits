package epicarchitect.breakbadhabits.di.declaration.foundation

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers

interface CoroutinesModule {
    val coroutineDispatchers: CoroutineDispatchers
}