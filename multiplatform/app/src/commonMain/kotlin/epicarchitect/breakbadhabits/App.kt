package epicarchitect.breakbadhabits

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.presentation.app.AppViewModel
import epicarchitect.breakbadhabits.screens.root.RootScreen

@Composable
fun App(appViewModel: AppViewModel) {
    RootScreen(appViewModel)
}