package epicarchitect.breakbadhabits.features.appSettings

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class AppSettingsDependencies(
    val navigation: AppSettingsNavigation,
    val resources: AppSettingsResources,
    val mainDatabase: MainDatabase
)