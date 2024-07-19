package epicarchitect.breakbadhabits.environment.resources

import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.environment.resources.icons.AppIcons
import epicarchitect.breakbadhabits.environment.resources.strings.app.EnglishAppStrings
import epicarchitect.breakbadhabits.environment.resources.strings.app.RussianAppStrings

class AppResources {
    val strings = when (Locale.current.language) {
        "ru" -> RussianAppStrings()
        else -> EnglishAppStrings()
    }
    val icons = AppIcons()
}