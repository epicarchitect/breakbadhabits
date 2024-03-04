package epicarchitect.breakbadhabits.di.declaration.main.presentation

//class HabitsPresentationModule(
//    private val habitsLogicModule: HabitsLogicModule,
//    private val dateTimeLogicModule: DateTimeLogicModule
//) : HabitsPresentationModule {
//
//    override fun habitCreationViewModel(
//        coroutineScope: CoroutineScope
//    ) = HabitCreationViewModel(
//        coroutineScope = coroutineScope,
//        habitCreator = habitsLogicModule.habitCreator,
//        habitNewNameValidator = habitsLogicModule.habitNewNameValidator,
//        trackTimeValidator = habitsLogicModule.habitTrackDateTimeRangeValidator,
//        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
//        iconProvider = habitsLogicModule.habitIconProvider,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider
//    )
//
//    override fun habitUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ) = HabitUpdatingViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitUpdater = habitsLogicModule.habitUpdater,
//        habitDeleter = habitsLogicModule.habitDeleter,
//        habitNewNameValidator = habitsLogicModule.habitNewNameValidator,
//        iconProvider = habitsLogicModule.habitIconProvider,
//        habitId = habitId
//    )
//
//    override fun habitDetailsViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ) = HabitDetailsViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitTrackProvider = habitsLogicModule.habitTrackProvider,
//        habitAbstinenceProvider = habitsLogicModule.habitAbstinenceProvider,
//        habitStatisticsProvider = habitsLogicModule.habitStatisticsProvider,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        habitId = habitId
//    )
//
//    override fun habitTrackCreationViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ) = HabitTrackCreationViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitTrackCreator = habitsLogicModule.habitTrackCreator,
//        trackRangeValidator = habitsLogicModule.habitTrackDateTimeRangeValidator,
//        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        habitId = habitId
//    )
//
//    override fun habitTracksViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ) = HabitTracksViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitTrackProvider = habitsLogicModule.habitTrackProvider,
//        habitId = habitId
//    )
//
//    override fun habitTrackUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitTrackId: Int
//    ) = HabitTrackUpdatingViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitTrackProvider = habitsLogicModule.habitTrackProvider,
//        habitTrackUpdater = habitsLogicModule.habitTrackUpdater,
//        habitTrackDeleter = habitsLogicModule.habitTrackDeleter,
//        trackRangeValidator = habitsLogicModule.habitTrackDateTimeRangeValidator,
//        trackEventCountValidator = habitsLogicModule.habitTrackEventCountValidator,
//        dateTimeProvider = dateTimeLogicModule.dateTimeProvider,
//        habitTrackId = habitTrackId
//    )
//
//    override fun habitAppWidgetsViewModel(
//        coroutineScope: CoroutineScope
//    ): HabitAppWidgetsViewModel = HabitAppWidgetsViewModel(
//        coroutineScope = coroutineScope,
//        habitWidgetProvider = habitsLogicModule.habitWidgetProvider,
//        habitProvider = habitsLogicModule.habitProvider
//    )
//
//    override fun habitWidgetUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitWidgetId: Int
//    ) = HabitAppWidgetUpdatingViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitWidgetProvider = habitsLogicModule.habitWidgetProvider,
//        habitWidgetUpdater = habitsLogicModule.habitWidgetUpdater,
//        habitWidgetDeleter = habitsLogicModule.habitWidgetDeleter,
//        habitWidgetId = habitWidgetId
//    )
//
//    override fun habitWidgetCreationViewModel(
//        coroutineScope: CoroutineScope,
//        widgetSystemId: Int
//    ) = HabitAppWidgetCreationViewModel(
//        coroutineScope = coroutineScope,
//        habitProvider = habitsLogicModule.habitProvider,
//        habitWidgetCreator = habitsLogicModule.habitWidgetCreator,
//        widgetSystemId = widgetSystemId
//    )
//}