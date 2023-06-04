import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.ui.app.AppScreen


fun main() {
    setupAppModuleHolder()
    application {
        Window(
            title = "Break Bad Habits",
            onCloseRequest = ::exitApplication
        ) {
            AppTheme(
                colorScheme = AppColorsSchemes.light
            ) {
                AppScreen(AppModuleHolder.current)
            }
        }
    }
}