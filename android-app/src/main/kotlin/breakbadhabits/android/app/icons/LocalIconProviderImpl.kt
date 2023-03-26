package breakbadhabits.android.app.icons

import breakbadhabits.android.app.R
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.app.logic.icons.LocalIconProvider

class LocalIconProviderImpl : LocalIconProvider {
    private val icons = listOf(
        create(0, R.drawable.icon_0),
        create(1, R.drawable.icon_1),
        create(2, R.drawable.icon_2),
        create(3, R.drawable.icon_3),
        create(4, R.drawable.icon_4),
        create(5, R.drawable.icon_5),
        create(6, R.drawable.icon_6),
        create(7, R.drawable.icon_7),
        create(8, R.drawable.icon_8),
        create(9, R.drawable.icon_9),
        create(10, R.drawable.icon_10),
        create(11, R.drawable.icon_11),
        create(12, R.drawable.icon_12),
        create(13, R.drawable.icon_13),
        create(14, R.drawable.icon_14),
        create(15, R.drawable.icon_15),
        create(16, R.drawable.icon_16),
        create(17, R.drawable.icon_17),
        create(18, R.drawable.icon_18),
        create(19, R.drawable.icon_19),
        create(20, R.drawable.icon_20),
        create(21, R.drawable.icon_21),
        create(22, R.drawable.icon_22),
        create(23, R.drawable.icon_23),
        create(24, R.drawable.icon_24),
        create(25, R.drawable.icon_25),
        create(26, R.drawable.icon_26),
        create(27, R.drawable.icon_27),
    )

    override fun getIcons() = icons

    override fun getIcon(id: LocalIcon.Id) = icons.first { it.id == id }

    private fun create(id: Long, resourceId: Int) = LocalIconImpl(LocalIcon.Id(id), resourceId)
}