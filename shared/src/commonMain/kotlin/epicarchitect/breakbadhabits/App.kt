package epicarchitect.breakbadhabits

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.screens.root.RootScreen

@Composable
fun App() {
    RootScreen(AppModuleHolder.require())
}