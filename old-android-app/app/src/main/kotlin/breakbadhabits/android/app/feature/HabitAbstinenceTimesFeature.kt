package breakbadhabits.android.app.feature

//class HabitAbstinenceTimesFeature(
//    coroutineScope: CoroutineScope,
//    habitsRepository: DefaultHabitsRepository,
//    habitId: Int
//) {
//
//    val state = combine(
//        habitsRepository.habitEventListByHabitIdFlow(habitId),
//        TikTik.everySecond()
//    ) { events, currentTime ->
//        val sortedEvents = events.sortedBy { it.time }
//        List(sortedEvents.size) { i ->
//            if (i == sortedEvents.indices.last) {
//                currentTime - sortedEvents[i].time
//            } else {
//                sortedEvents[i + 1].time - sortedEvents[i].time
//            }
//        }
//    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), emptyList())
//
//}