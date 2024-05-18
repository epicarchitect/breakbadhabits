package epicarchitect.breakbadhabits.data.resources

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.data.resources.icons.AppIcons
import epicarchitect.breakbadhabits.data.resources.strings.LocalizedAppStrings

class AppResources(locale: Locale) {
    val strings = LocalizedAppStrings(locale)
    val icons = AppIcons()
}