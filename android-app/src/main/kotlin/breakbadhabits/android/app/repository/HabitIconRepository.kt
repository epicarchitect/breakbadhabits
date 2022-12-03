package breakbadhabits.android.app.repository

import breakbadhabits.entity.Habit

class HabitIconRepository {

    val list: List<Habit.IconResource> = ArrayList<Habit.IconResource>().apply {
        repeat(28) {
            add(Habit.IconResource(it))
        }
    }
//
//    private fun resolveResource(iconId: Int) = try {
//        context.resources.getIdentifier(
//            "icon_$iconId",
//            "drawable",
//            context.packageName
//        )
//    } catch (t: Throwable) {
//        R.drawable.ic_error
//    }
}