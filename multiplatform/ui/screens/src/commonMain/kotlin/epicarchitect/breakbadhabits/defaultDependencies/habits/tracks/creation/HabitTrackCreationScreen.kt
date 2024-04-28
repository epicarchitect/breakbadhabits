package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.tracks.creation.HabitTrackCreation
import epicarchitect.breakbadhabits.features.habits.tracks.creation.HabitTrackCreationDependencies

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackCreation(
            dependencies = HabitTrackCreationDependencies(
                habitId = habitId,
                navigation = DefaultHabitTrackCreationNavigation(LocalNavigator.currentOrThrow),
                resources = LocalizedHabitTrackCreationResources(Locale.current),
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}