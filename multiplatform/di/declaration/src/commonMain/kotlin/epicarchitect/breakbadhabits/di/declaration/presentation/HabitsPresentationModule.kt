package epicarchitect.breakbadhabits.di.declaration.presentation
//
//import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetCreationViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetUpdatingViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitAppWidgetsViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitCreationViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitDetailsViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitTrackUpdatingViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitTracksViewModel
//import epicarchitect.breakbadhabits.presentation.habits.HabitUpdatingViewModel
//import kotlinx.coroutines.CoroutineScope
//
//interface HabitsPresentationModule {
//    fun habitCreationViewModel(
//        coroutineScope: CoroutineScope
//    ): HabitCreationViewModel
//
//    fun habitUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ): HabitUpdatingViewModel
//
//    fun habitDetailsViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ): HabitDetailsViewModel
//
//    fun habitTrackCreationViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ): HabitTrackCreationViewModel
//
//    fun habitTracksViewModel(
//        coroutineScope: CoroutineScope,
//        habitId: Int
//    ): HabitTracksViewModel
//
//    fun habitTrackUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitTrackId: Int
//    ): HabitTrackUpdatingViewModel
//
//    fun habitAppWidgetsViewModel(
//        coroutineScope: CoroutineScope
//    ): HabitAppWidgetsViewModel
//
//    fun habitWidgetUpdatingViewModel(
//        coroutineScope: CoroutineScope,
//        habitWidgetId: Int
//    ): HabitAppWidgetUpdatingViewModel
//
//    fun habitWidgetCreationViewModel(
//        coroutineScope: CoroutineScope,
//        widgetSystemId: Int
//    ): HabitAppWidgetCreationViewModel
//}