package epicarchitect.breakbadhabits.di.declaration.impl.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule
import epicarchitect.breakbadhabits.foundation.coroutines.DefaultCoroutineDispatchers

class CoroutinesModuleImpl : CoroutinesModule {
    override val coroutineDispatchers by lazy {
        DefaultCoroutineDispatchers()
    }
}