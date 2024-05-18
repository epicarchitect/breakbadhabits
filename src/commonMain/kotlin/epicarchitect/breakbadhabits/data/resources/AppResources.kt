package epicarchitect.breakbadhabits.data.resources

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.data.resources.icons.AppIcons
import epicarchitect.breakbadhabits.data.resources.strings.LocalizedAppStrings

class AppResources {
    // TODO: update by locale from platform and from settings?
    val strings = LocalizedAppStrings(Locale.current)
    val icons = AppIcons()
}