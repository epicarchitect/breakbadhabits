package epicarchitect.breakbadhabits.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.foundation.uikit.button.Button
import epicarchitect.breakbadhabits.foundation.uikit.button.RadioButton
import epicarchitect.breakbadhabits.foundation.uikit.text.Text

val LocalAppSettingsResources = compositionLocalOf<AppSettingsResources> {
    error("LocalAppSettingsResources not provided")
}

data class AppSettingsResources(
    val titleText: String,
    val themeSelectionDescription: String,
    val themeSelectionSystemTheme: String,
    val themeSelectionDarkTheme: String,
    val themeSelectionLightTheme: String,
    val widgetsDescription: String,
    val widgetsButton: String,
)

@Composable
fun AppSettings(
    openWidgetSettings: () -> Unit
) {
    val resources = LocalAppSettingsResources.current
//    val darkModeManager = LocalDarkModeManager.current
//    val darkMode by darkModeManager.mode
//
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = resources.titleText,
            type = Text.Type.Title,
            priority = Text.Priority.High
        )

        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 8.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
            text = resources.themeSelectionDescription,
        )

        RadioButton(
            text = resources.themeSelectionSystemTheme,
            selected = true,//darkMode == DarkMode.BY_SYSTEM,
            onSelect = {
//                darkModeManager.changeMode(DarkMode.BY_SYSTEM)
            }
        )

        RadioButton(
            text = resources.themeSelectionLightTheme,
            selected = false, //darkMode == DarkMode.DISABLED,
            onSelect = {
//                darkModeManager.changeMode(DarkMode.DISABLED)
            }
        )

        RadioButton(
            text = resources.themeSelectionDarkTheme,
            selected = false,//darkMode == DarkMode.ENABLED,
            onSelect = {
//                darkModeManager.changeMode(DarkMode.ENABLED)
            }
        )

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 4.dp
            ),
            text = resources.widgetsDescription
        )

        Button(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 16.dp
            ),
            onClick = openWidgetSettings,
            text = resources.widgetsButton
        )
    }
}