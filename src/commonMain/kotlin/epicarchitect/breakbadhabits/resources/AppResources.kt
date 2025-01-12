package epicarchitect.breakbadhabits.resources

import epicarchitect.breakbadhabits.language.AppLanguage
import epicarchitect.breakbadhabits.language.PlatformLanguageProvider
import epicarchitect.breakbadhabits.resources.strings.app.EnglishAppStrings
import epicarchitect.breakbadhabits.resources.strings.app.RussianAppStrings

class AppResources(
    languageProvider: PlatformLanguageProvider
) {
    val strings = when (languageProvider.language) {
        AppLanguage.RUSSIAN -> RussianAppStrings()
        AppLanguage.ENGLISH -> EnglishAppStrings()
    }
}