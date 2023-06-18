package epicarchitect.breakbadhabits.di.declaration.main.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule
import epicarchitect.breakbadhabits.foundation.coroutines.DefaultCoroutineDispatchers

class CoroutinesModule : CoroutinesModule {
    override val coroutineDispatchers = DefaultCoroutineDispatchers
}
