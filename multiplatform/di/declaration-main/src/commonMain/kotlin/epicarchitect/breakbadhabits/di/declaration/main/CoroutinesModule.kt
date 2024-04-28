package epicarchitect.breakbadhabits.di.declaration.main

import epicarchitect.breakbadhabits.di.declaration.CoroutinesModule
import epicarchitect.breakbadhabits.foundation.coroutines.DefaultCoroutineDispatchers

class CoroutinesModule : CoroutinesModule {
    override val coroutineDispatchers = DefaultCoroutineDispatchers
}