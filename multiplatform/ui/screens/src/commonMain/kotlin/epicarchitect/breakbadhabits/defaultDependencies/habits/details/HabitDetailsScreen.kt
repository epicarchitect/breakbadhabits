package epicarchitect.breakbadhabits.defaultDependencies.habits.details

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.HabitIcons
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.details.HabitDetails
import epicarchitect.breakbadhabits.features.habits.details.HabitDetailsDependencies

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitDetails(
            dependencies = HabitDetailsDependencies(
                habitId = habitId,
                navigation = DefaultHabitDetailsNavigation(LocalNavigator.currentOrThrow, habitId),
                resources = LocalizedHabitDetailsResources(Locale.current),
                habitIcons = HabitIcons,
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}