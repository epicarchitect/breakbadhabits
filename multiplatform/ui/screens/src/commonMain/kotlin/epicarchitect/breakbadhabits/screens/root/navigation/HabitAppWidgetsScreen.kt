package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.widgets.HabitAppWidgets
import epicarchitect.breakbadhabits.screens.hold

class HabitAppWidgetsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold(factory = presentationModule.habits::habitAppWidgetsViewModel)
        HabitAppWidgets(
            itemsController = viewModel.itemsController,
            onWidgetClick = {
                navigator += HabitAppWidgetUpdatingScreen(it)
            }
        )
    }
}
