package epicarchitect.breakbadhabits

import android.content.res.Configuration
import android.content.res.Resources


fun isSystemDarkModeEnabled() = Resources.getSystem().configuration
    .uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES