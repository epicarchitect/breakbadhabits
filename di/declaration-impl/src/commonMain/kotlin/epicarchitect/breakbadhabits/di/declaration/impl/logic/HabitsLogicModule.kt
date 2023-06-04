package epicarchitect.breakbadhabits.di.declaration.impl.logic

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule
import epicarchitect.breakbadhabits.di.declaration.logic.DateTimeLogicModule
import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.di.declaration.foundation.IdentificationModule
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModule
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitTrackCreator
import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
import epicarchitect.breakbadhabits.logic.habits.provider.HabitsConfigProvider
import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackTimeValidator

class HabitsLogicModuleImpl(
    coroutinesModule: CoroutinesModule,
    dateTimeLogicModule: DateTimeLogicModule,
    identificationModule: IdentificationModule,
    mainDatabaseModule: MainDatabaseModule,
    external: HabitsLogicModuleExternal
) : HabitsLogicModule, HabitsLogicModuleExternal by external {
    override val habitAbstinenceProvider by lazy {
        HabitAbstinenceProvider(
            coroutineDispatchers = coroutinesModule.coroutineDispatchers,
            dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
            habitTrackProvider = habitTrackProvider
        )
    }
    override val habitCreator by lazy {
        HabitCreator(
            idGenerator = identificationModule.idGenerator,
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitsConfigProvider by lazy {
        HabitsConfigProvider()
    }

    override val habitNewNameValidator by lazy {
        HabitNewNameValidator(
            mainDatabase = mainDatabaseModule.mainDatabase,
            configProvider = habitsConfigProvider,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitTrackTimeValidator by lazy {
        HabitTrackTimeValidator(
            dateTimeProvider = dateTimeLogicModule.dateTimeProvider
        )
    }
    override val habitTrackEventCountValidator by lazy {
        HabitTrackEventCountValidator()
    }
    override val habitDeleter by lazy {
        HabitDeleter(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitProvider by lazy {
        HabitProvider(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers,
            iconProvider = habitIconProvider
        )
    }
    override val habitUpdater by lazy {
        HabitUpdater(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitTrackUpdater by lazy {
        HabitTrackUpdater(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitTrackDeleter by lazy {
        HabitTrackDeleter(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitStatisticsProvider by lazy {
        HabitStatisticsProvider(
            habitTrackProvider = habitTrackProvider,
            habitAbstinenceProvider = habitAbstinenceProvider,
            dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitTrackCreator by lazy {
        HabitTrackCreator(
            mainDatabase = mainDatabaseModule.mainDatabase,
            idGenerator = identificationModule.idGenerator,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitTrackProvider by lazy {
        HabitTrackProvider(
            mainDatabase = mainDatabaseModule.mainDatabase,
            dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitWidgetProvider by lazy {
        HabitWidgetProvider(
            coroutineDispatchers = coroutinesModule.coroutineDispatchers,
            mainDatabase = mainDatabaseModule.mainDatabase
        )
    }
    override val habitWidgetCreator by lazy {
        HabitWidgetCreator(
            mainDatabase = mainDatabaseModule.mainDatabase,
            idGenerator = identificationModule.idGenerator,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitWidgetUpdater by lazy {
        HabitWidgetUpdater(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
    override val habitWidgetDeleter by lazy {
        HabitWidgetDeleter(
            mainDatabase = mainDatabaseModule.mainDatabase,
            coroutineDispatchers = coroutinesModule.coroutineDispatchers
        )
    }
}

interface HabitsLogicModuleExternal {
    val habitIconProvider: IconProvider
}
