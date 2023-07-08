package breakbadhabits.foundation.icons.android

import epicarchitect.breakbadhabits.foundation.icons.Icon

data class AndroidResourceIcon(
    override val id: Int,
    val resourceId: Int
) : Icon

val Icon.android get() = this as AndroidResourceIcon

val Icon.resourceId get() = android.resourceId