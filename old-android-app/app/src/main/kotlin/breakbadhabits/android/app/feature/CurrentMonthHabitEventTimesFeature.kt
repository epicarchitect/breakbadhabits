package breakbadhabits.android.app.feature

//class CurrentMonthHabitEventTimesFeature(
//    coroutineScope: CoroutineScope,
//    habitEventTimesFeature: HabitEventTimesFeature
//) {
//
//    val state = habitEventTimesFeature.state.map {
//        it.filter {
//            monthEquals(
//                Calendar.getInstance().apply { timeInMillis = it },
//                Calendar.getInstance()
//            )
//        }
//    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())
//
//}