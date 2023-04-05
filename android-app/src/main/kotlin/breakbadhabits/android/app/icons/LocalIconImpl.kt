package breakbadhabits.android.app.icons

import breakbadhabits.app.logic.icons.LocalIcon

data class LocalIconImpl(
    override val id: Int,
    val resourceId: Int
) : LocalIcon

val LocalIcon.impl get() = this as LocalIconImpl

val LocalIcon.resourceId get() = impl.resourceId