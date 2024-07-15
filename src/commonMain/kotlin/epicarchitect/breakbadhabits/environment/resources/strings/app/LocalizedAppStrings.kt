package epicarchitect.breakbadhabits.environment.resources.strings.app

import epicarchitect.breakbadhabits.environment.language.AppLanguage

class LocalizedAppStrings(language: AppLanguage) : AppStrings by resolve(language)

private fun resolve(language: AppLanguage) = when (language) {
    AppLanguage.RUSSIAN -> RussianAppStrings()
    AppLanguage.ENGLISH -> EnglishAppStrings()
}