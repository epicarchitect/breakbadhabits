package breakbadhabits.android.app.feature

//class HabitAverageAbstinenceTimeFeature(
//    coroutineScope: CoroutineScope,
//    habitAbstinenceTimesFeature: HabitAbstinenceTimesFeature
//) {
//
//    val state = habitAbstinenceTimesFeature.state.map {
//        if (it.isEmpty()) null
//        else it.average().toLong()
//    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)
//
//}