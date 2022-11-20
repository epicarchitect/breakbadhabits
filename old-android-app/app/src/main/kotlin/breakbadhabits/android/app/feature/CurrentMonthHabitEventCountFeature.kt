package breakbadhabits.android.app.feature

//class CurrentMonthHabitEventCountFeature(
//    coroutineScope: CoroutineScope,
//    habitsRepository: DefaultHabitsRepository,
//    habitId: Int
//) {
//
//    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
//        events.count {
//            monthEquals(
//                Calendar.getInstance().apply { timeInMillis = it.time },
//                Calendar.getInstance()
//            )
//        }
//    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), 0)
//
//}