package epicarchitect.breakbadhabits

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.ui.app.AppScreen

@Composable
fun App() {
    AppScreen(AppModuleHolder.current)
}