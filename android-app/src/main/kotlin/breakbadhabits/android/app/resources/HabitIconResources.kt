package breakbadhabits.android.app.resources

import android.content.Context
import breakbadhabits.android.app.R

class HabitIconResources(private val context: Context) {

    val icons: List<Icon> = ArrayList<Icon>().apply {
        repeat(28) {
            add(Icon(it.toLong(), resolveResource(it)))
        }
    }

    fun getByResourceId(resourceId: Int) = icons.first { it.resourceId == resourceId }

    private fun resolveResource(iconId: Int) = try {
        context.resources.getIdentifier(
            "icon_$iconId",
            "drawable",
            context.packageName
        )
    } catch (t: Throwable) {
        R.drawable.ic_error
    }

    operator fun get(iconId: Long) = icons.find { it.iconId == iconId }!!.resourceId

    data class Icon(val iconId: Long, val resourceId: Int)
}