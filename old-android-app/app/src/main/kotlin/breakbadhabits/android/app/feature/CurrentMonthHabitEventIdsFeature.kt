package breakbadhabits.android.app.feature

//class CurrentMonthHabitEventIdsFeature(
//    coroutineScope: CoroutineScope,
//    habitsRepository: DefaultHabitsRepository,
//    habitId: Int
//) {
//
//    val state = habitsRepository.habitEventListByHabitIdFlow(habitId).map { events ->
//        events.filter {
//            monthEquals(
//                Calendar.getInstance().apply { timeInMillis = it.time },
//                Calendar.getInstance()
//            )
//        }.map { it.id }
//    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())
//
//}