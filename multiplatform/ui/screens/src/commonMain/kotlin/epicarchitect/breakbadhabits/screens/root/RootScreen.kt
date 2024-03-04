package epicarchitect.breakbadhabits.screens.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.intl.Locale
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.presentation.app.AppViewModel
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitDetailsViewModel
import epicarchitect.breakbadhabits.presentation.habits.HabitTrackCreationViewModel
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.dashboard.Dashboard
import epicarchitect.breakbadhabits.screens.dashboard.LocalDashboardResources
import epicarchitect.breakbadhabits.screens.dashboardResourcesOf
import epicarchitect.breakbadhabits.screens.habitTrackCreationResourcesOf
import epicarchitect.breakbadhabits.screens.habits.HabitDetails
import epicarchitect.breakbadhabits.screens.habits.tracks.HabitTrackCreation
import epicarchitect.breakbadhabits.screens.habits.tracks.LocalHabitTrackCreationResources

@Composable
fun RootScreen(appViewModel: AppViewModel) {
    CompositionLocalProvider(
        LocalAppModule provides appViewModel.appModule,
        LocalDashboardResources provides dashboardResourcesOf(Locale.current),
        LocalHabitTrackCreationResources provides habitTrackCreationResourcesOf(Locale.current)
    ) {
        AppTheme(
            colorScheme = if (isSystemInDarkTheme()) {
                AppColorsSchemes.dark
            } else {
                AppColorsSchemes.light
            }
        ) {
            val viewModelStack by appViewModel.navigation.collectAsState()
            when (val viewModel = viewModelStack.lastOrNull()) {
                is DashboardViewModel -> {
                    Dashboard(
                        viewModel = viewModel,
                        onHabitCreationClick = {
//                                navigator += HabitCreationScreen()
                        },
                        onAppSettingsClick = {
//                                navigator += AppSettingsScreen()
                        }
                    )
                }

                is HabitDetailsViewModel -> {
                    HabitDetails(viewModel)
                }

                is HabitTrackCreationViewModel -> {
                    HabitTrackCreation(viewModel)
                }
            }
//            Navigator(DashboardScreen())
        }
    }
}