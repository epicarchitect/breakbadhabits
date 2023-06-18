package epicarchitect.breakbadhabits.di.declaration.main.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule
import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider

class DateTimeLogicModule(
    coroutinesModule: CoroutinesModule
) : DateTimeLogicModule {
    override val dateTimeProvider by lazy {
        DateTimeProvider(
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
}
