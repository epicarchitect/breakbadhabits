package epicarchitect.breakbadhabits.di.declaration.impl.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule
import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider

class DateTimeLogicModuleImpl(
    coroutinesModule: CoroutinesModule
) : DateTimeLogicModule {
    override val dateTimeProvider by lazy {
        DateTimeProvider(
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
}