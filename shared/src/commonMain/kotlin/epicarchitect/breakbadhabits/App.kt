package epicarchitect.breakbadhabits

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.ui.app.RootScreen

@Composable
fun App() {
    CompositionLocalProvider(
        LocalAppModule provides AppModuleHolder.current
    ) {
        RootScreen()
    }
}