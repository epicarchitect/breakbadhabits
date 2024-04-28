package epicarchitect.breakbadhabits.defaultDependencies.habits.editing

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.HabitIcons
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.editing.HabitEditing
import epicarchitect.breakbadhabits.features.habits.editing.HabitEditingDependencies

class HabitEditingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitEditing(
            dependencies = HabitEditingDependencies(
                habitId = habitId,
                navigation = DefaultHabitEditingNavigation(LocalNavigator.currentOrThrow),
                resources = LocalizedHabitEditingResources(Locale.current),
                mainDatabase = LocalAppModule.current.mainDatabase,
                habitIcons = HabitIcons
            )
        )
    }
}