package breakbadhabits.android.app.ui.habits.resources

import breakbadhabits.android.app.R
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.provider.HabitIconProvider

class HabitIconResourceProvider(habitIconProvider: HabitIconProvider) {

    private val icons = habitIconProvider.provide().map {
        HabitIconResource(
            icon = it,
            resourceId = resolveResource(it.iconId.toInt())
        )
    }

    operator fun get(icon: Habit.Icon) = icons.first {
        it.icon == icon
    }

    private fun resolveResource(iconId: Int) = when (iconId) {
        0 -> R.drawable.icon_0
        1 -> R.drawable.icon_1
        2 -> R.drawable.icon_2
        3 -> R.drawable.icon_3
        4 -> R.drawable.icon_4
        5 -> R.drawable.icon_5
        6 -> R.drawable.icon_6
        7 -> R.drawable.icon_7
        8 -> R.drawable.icon_8
        9 -> R.drawable.icon_9
        10 -> R.drawable.icon_10
        11 -> R.drawable.icon_11
        12 -> R.drawable.icon_12
        13 -> R.drawable.icon_13
        14 -> R.drawable.icon_14
        15 -> R.drawable.icon_15
        16 -> R.drawable.icon_16
        17 -> R.drawable.icon_17
        18 -> R.drawable.icon_18
        19 -> R.drawable.icon_19
        20 -> R.drawable.icon_20
        21 -> R.drawable.icon_21
        22 -> R.drawable.icon_22
        23 -> R.drawable.icon_23
        24 -> R.drawable.icon_24
        25 -> R.drawable.icon_25
        26 -> R.drawable.icon_26
        27 -> R.drawable.icon_27
        else -> error("Unexpected iconId: $iconId")
    }
}