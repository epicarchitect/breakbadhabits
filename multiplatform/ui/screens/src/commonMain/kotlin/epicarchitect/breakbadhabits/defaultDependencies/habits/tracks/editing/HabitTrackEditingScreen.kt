package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.editing

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.tracks.editing.HabitTrackEditing
import epicarchitect.breakbadhabits.features.habits.tracks.editing.HabitTrackEditingDependencies

class HabitTrackEditingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTrackEditing(
            dependencies = HabitTrackEditingDependencies(
                habitTrackId = habitTrackId,
                navigation = DefaultHabitTrackEditingNavigation(LocalNavigator.currentOrThrow),
                resources = LocalizedHabitTrackEditingResources(Locale.current),
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}