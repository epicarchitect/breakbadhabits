package epicarchitect.breakbadhabits

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    setupAppModuleHolder()
    application {
        Window(
            title = "Break Bad Habits",
            onCloseRequest = ::exitApplication
        ) {
            App()
        }
    }
}