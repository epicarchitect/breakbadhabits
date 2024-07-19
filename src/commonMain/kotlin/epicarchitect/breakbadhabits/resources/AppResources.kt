package epicarchitect.breakbadhabits.resources

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.resources.icons.AppIcons
import epicarchitect.breakbadhabits.resources.strings.app.EnglishAppStrings
import epicarchitect.breakbadhabits.resources.strings.app.RussianAppStrings

class AppResources {
    val strings = when (Locale.current.language) {
        "ru" -> RussianAppStrings()
        else -> EnglishAppStrings()
    }
    val icons = AppIcons()
}