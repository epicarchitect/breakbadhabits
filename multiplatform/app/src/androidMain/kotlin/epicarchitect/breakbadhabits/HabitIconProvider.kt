package epicarchitect.breakbadhabits

import breakbadhabits.foundation.icons.android.AndroidResourceIcon
import epicarchitect.breakbadhabits.foundation.icons.IconProvider
import epicarchitect.breakbadhabits.multiplatform.app.R
import kotlinx.coroutines.flow.flowOf

class HabitIconProvider : IconProvider {
    private val icons = listOf(
        AndroidResourceIcon(0, R.drawable.habit_icon_0),
        AndroidResourceIcon(1, R.drawable.habit_icon_1),
        AndroidResourceIcon(2, R.drawable.habit_icon_2),
        AndroidResourceIcon(3, R.drawable.habit_icon_3),
        AndroidResourceIcon(4, R.drawable.habit_icon_4),
        AndroidResourceIcon(5, R.drawable.habit_icon_5),
        AndroidResourceIcon(6, R.drawable.habit_icon_6),
        AndroidResourceIcon(7, R.drawable.habit_icon_7),
        AndroidResourceIcon(8, R.drawable.habit_icon_8),
        AndroidResourceIcon(9, R.drawable.habit_icon_9),
        AndroidResourceIcon(10, R.drawable.habit_icon_10),
        AndroidResourceIcon(11, R.drawable.habit_icon_11),
        AndroidResourceIcon(12, R.drawable.habit_icon_12),
        AndroidResourceIcon(13, R.drawable.habit_icon_13),
        AndroidResourceIcon(14, R.drawable.habit_icon_14),
        AndroidResourceIcon(15, R.drawable.habit_icon_15),
        AndroidResourceIcon(16, R.drawable.habit_icon_16),
        AndroidResourceIcon(17, R.drawable.habit_icon_17),
        AndroidResourceIcon(18, R.drawable.habit_icon_18),
        AndroidResourceIcon(19, R.drawable.habit_icon_19),
        AndroidResourceIcon(20, R.drawable.habit_icon_20),
        AndroidResourceIcon(21, R.drawable.habit_icon_21),
        AndroidResourceIcon(22, R.drawable.habit_icon_22),
        AndroidResourceIcon(23, R.drawable.habit_icon_23),
        AndroidResourceIcon(24, R.drawable.habit_icon_24),
        AndroidResourceIcon(25, R.drawable.habit_icon_25),
        AndroidResourceIcon(26, R.drawable.habit_icon_26),
        AndroidResourceIcon(27, R.drawable.habit_icon_27)
    )

    override fun iconsFlow() = flowOf(icons)
}