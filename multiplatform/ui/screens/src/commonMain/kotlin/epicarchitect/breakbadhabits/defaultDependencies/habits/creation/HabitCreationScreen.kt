package epicarchitect.breakbadhabits.defaultDependencies.habits.creation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.HabitIcons
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.creation.HabitCreation
import epicarchitect.breakbadhabits.features.habits.creation.HabitCreationDependencies

class HabitCreationScreen : Screen {
    @Composable
    override fun Content() {
        HabitCreation(
            dependencies = HabitCreationDependencies(
                navigation = DefaultHabitCreationNavigation(LocalNavigator.currentOrThrow),
                resources = LocalizedHabitCreationResources(Locale.current),
                mainDatabase = LocalAppModule.current.mainDatabase,
                idGenerator = LocalAppModule.current.identification.idGenerator,
                habitIcons = HabitIcons
            )
        )
    }
}