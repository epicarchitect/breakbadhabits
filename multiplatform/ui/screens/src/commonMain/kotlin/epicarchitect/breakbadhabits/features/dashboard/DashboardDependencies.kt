package epicarchitect.breakbadhabits.features.dashboard

import epicarchitect.breakbadhabits.foundation.icons.Icons
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class DashboardDependencies(
    val resources: DashboardResources,
    val navigation: DashboardNavigation,
    val mainDatabase: MainDatabase,
    val habitIcons: Icons
)