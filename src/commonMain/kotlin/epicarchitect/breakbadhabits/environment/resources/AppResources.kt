package epicarchitect.breakbadhabits.environment.resources

import epicarchitect.breakbadhabits.environment.language.ActualAppLanguage
import epicarchitect.breakbadhabits.environment.resources.icons.AppIcons
import epicarchitect.breakbadhabits.environment.resources.strings.app.ActualAppStrings
import kotlinx.coroutines.CoroutineScope

class AppResources(
    coroutineScope: CoroutineScope,
    actualAppLanguage: ActualAppLanguage
) {
    val strings = ActualAppStrings(coroutineScope, actualAppLanguage)
    val icons = AppIcons()
}