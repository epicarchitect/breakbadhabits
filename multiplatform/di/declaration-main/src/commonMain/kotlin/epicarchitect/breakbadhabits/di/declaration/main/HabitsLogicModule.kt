package epicarchitect.breakbadhabits.di.declaration.main
//
//import epicarchitect.breakbadhabits.di.declaration.CoroutinesModule
//import epicarchitect.breakbadhabits.di.declaration.DateTimeLogicModule
//import epicarchitect.breakbadhabits.di.declaration.HabitsLogicModule
//import epicarchitect.breakbadhabits.di.declaration.IdentificationModule
//import epicarchitect.breakbadhabits.foundation.icons.Icons
//import epicarchitect.breakbadhabits.logic.habits.creator.HabitCreator
//import epicarchitect.breakbadhabits.logic.habits.creator.HabitTrackCreator
//import epicarchitect.breakbadhabits.logic.habits.creator.HabitWidgetCreator
//import epicarchitect.breakbadhabits.logic.habits.deleter.HabitDeleter
//import epicarchitect.breakbadhabits.logic.habits.deleter.HabitTrackDeleter
//import epicarchitect.breakbadhabits.logic.habits.deleter.HabitWidgetDeleter
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitAbstinenceProvider
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitProvider
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitStatisticsProvider
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitTrackProvider
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitWidgetProvider
//import epicarchitect.breakbadhabits.logic.habits.provider.HabitsConfigProvider
//import epicarchitect.breakbadhabits.logic.habits.updater.HabitTrackUpdater
//import epicarchitect.breakbadhabits.logic.habits.updater.HabitUpdater
//import epicarchitect.breakbadhabits.logic.habits.updater.HabitWidgetUpdater
//import epicarchitect.breakbadhabits.logic.habits.validator.HabitNewNameValidator
//import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackDateTimeRangeValidator
//import epicarchitect.breakbadhabits.logic.habits.validator.HabitTrackEventCountValidator
//
//class HabitsLogicModule(
//    coroutinesModule: CoroutinesModule,
//    dateTimeLogicModule: DateTimeLogicModule,
//    identificationModule: IdentificationModule,
//    mainDatabaseModule: MainDatabaseModule,
//    externals: HabitsLogicModuleExternals
//) : HabitsLogicModule, HabitsLogicModuleExternals by externals {
//
//    override val habitCreator = HabitCreator(
//        idGenerator = identificationModule.idGenerator,
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
//    )
//
//    override val habitsConfigProvider = HabitsConfigProvider()
//
//    override val habitNewNameValidator = HabitNewNameValidator(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        configProvider = habitsConfigProvider,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitTrackDateTimeRangeValidator = HabitTrackDateTimeRangeValidator(
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
//    )
//
//    override val habitTrackEventCountValidator = HabitTrackEventCountValidator()
//
//    override val habitDeleter = HabitDeleter(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitProvider = HabitProvider(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        icons = habitIcons
//    )
//
//    override val habitUpdater = HabitUpdater(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitTrackUpdater = HabitTrackUpdater(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
//    )
//
//    override val habitTrackDeleter = HabitTrackDeleter(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitTrackCreator = HabitTrackCreator(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        idGenerator = identificationModule.idGenerator,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
//    )
//
//    override val habitTrackProvider = HabitTrackProvider(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitWidgetProvider = HabitWidgetProvider(
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        mainDatabase = mainDatabaseModule.mainDatabase
//    )
//
//    override val habitWidgetCreator = HabitWidgetCreator(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        idGenerator = identificationModule.idGenerator,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitWidgetUpdater = HabitWidgetUpdater(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitWidgetDeleter = HabitWidgetDeleter(
//        mainDatabase = mainDatabaseModule.mainDatabase,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//
//    override val habitAbstinenceProvider = HabitAbstinenceProvider(
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        habitTrackProvider = habitTrackProvider
//    )
//
//    override val habitStatisticsProvider = HabitStatisticsProvider(
//        habitTrackProvider = habitTrackProvider,
//        habitAbstinenceProvider = habitAbstinenceProvider,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        coroutineDispatchers = coroutinesModule.coroutineDispatchers
//    )
//}
//
//interface HabitsLogicModuleExternals {
//    val habitIcons: Icons
//}