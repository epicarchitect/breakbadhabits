package breakbadhabits.android.app.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.android.app.base.activity.DarkMode
import breakbadhabits.android.app.base.activity.LocalDarkModeManager
import breakbadhabits.foundation.uikit.button.Button
import breakbadhabits.foundation.uikit.button.RadioButton
import breakbadhabits.foundation.uikit.text.Text

@Composable
fun AppSettingsScreen(
    openWidgetSettings: () -> Unit
) {
    val darkModeManager = LocalDarkModeManager.current
    val darkMode by darkModeManager.mode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.main_settings),
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
            text = stringResource(R.string.appSettings_themeSelection_description),
        )

        RadioButton(
            text = stringResource(R.string.appSettings_themeSelection_systemTheme),
            selected = darkMode == DarkMode.BY_SYSTEM,
            onSelect = {
                darkModeManager.changeMode(DarkMode.BY_SYSTEM)
            }
        )

        RadioButton(
            text = stringResource(R.string.appSettings_themeSelection_lightTheme),
            selected = darkMode == DarkMode.DISABLED,
            onSelect = {
                darkModeManager.changeMode(DarkMode.DISABLED)
            }
        )

        RadioButton(
            text = stringResource(R.string.appSettings_themeSelection_darkTheme),
            selected = darkMode == DarkMode.ENABLED,
            onSelect = {
                darkModeManager.changeMode(DarkMode.ENABLED)
            }
        )

        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 4.dp
            ),
            text = stringResource(R.string.appSettings_widgets_description)
        )

        Button(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 16.dp
            ),
            onClick = openWidgetSettings,
            text = stringResource(R.string.appSettings_widgets_button)
        )
    }
}