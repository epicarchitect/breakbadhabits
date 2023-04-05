package breakbadhabits.android.app.icons

import breakbadhabits.android.app.R
import breakbadhabits.app.logic.icons.LocalIconProvider

class HabitLocalIconProvider : LocalIconProvider {
    private val icons = listOf(
        LocalIconImpl(0, R.drawable.habit_icon_0),
        LocalIconImpl(1, R.drawable.habit_icon_1),
        LocalIconImpl(2, R.drawable.habit_icon_2),
        LocalIconImpl(3, R.drawable.habit_icon_3),
        LocalIconImpl(4, R.drawable.habit_icon_4),
        LocalIconImpl(5, R.drawable.habit_icon_5),
        LocalIconImpl(6, R.drawable.habit_icon_6),
        LocalIconImpl(7, R.drawable.habit_icon_7),
        LocalIconImpl(8, R.drawable.habit_icon_8),
        LocalIconImpl(9, R.drawable.habit_icon_9),
        LocalIconImpl(10, R.drawable.habit_icon_10),
        LocalIconImpl(11, R.drawable.habit_icon_11),
        LocalIconImpl(12, R.drawable.habit_icon_12),
        LocalIconImpl(13, R.drawable.habit_icon_13),
        LocalIconImpl(14, R.drawable.habit_icon_14),
        LocalIconImpl(15, R.drawable.habit_icon_15),
        LocalIconImpl(16, R.drawable.habit_icon_16),
        LocalIconImpl(17, R.drawable.habit_icon_17),
        LocalIconImpl(18, R.drawable.habit_icon_18),
        LocalIconImpl(19, R.drawable.habit_icon_19),
        LocalIconImpl(20, R.drawable.habit_icon_20),
        LocalIconImpl(21, R.drawable.habit_icon_21),
        LocalIconImpl(22, R.drawable.habit_icon_22),
        LocalIconImpl(23, R.drawable.habit_icon_23),
        LocalIconImpl(24, R.drawable.habit_icon_24),
        LocalIconImpl(25, R.drawable.habit_icon_25),
        LocalIconImpl(26, R.drawable.habit_icon_26),
        LocalIconImpl(27, R.drawable.habit_icon_27),
    )

    override fun getIcons() = icons

    override fun getIcon(id: Int) = icons.first { it.id == id }
}