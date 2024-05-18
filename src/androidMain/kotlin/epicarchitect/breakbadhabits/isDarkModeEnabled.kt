package epicarchitect.breakbadhabits

import android.content.res.Configuration
import android.content.res.Resources

fun isDarkModeEnabled() = Resources.getSystem().configuration.let {
    it.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}