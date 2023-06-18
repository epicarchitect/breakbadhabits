package epicarchitect.breakbadhabits.di.declaration.logic

import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider

interface DateTimeLogicModule {
    val dateTimeProvider: DateTimeProvider
}
