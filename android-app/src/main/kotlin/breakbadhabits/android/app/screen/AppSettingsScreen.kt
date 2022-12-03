package breakbadhabits.android.app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.R
import breakbadhabits.ui.kit.Button
import breakbadhabits.ui.kit.RadioButton
import breakbadhabits.ui.kit.Text
import breakbadhabits.ui.kit.Title
import breakbadhabits.ui.kit.activity.DarkMode
import breakbadhabits.ui.kit.activity.LocalDarkModeManager

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
        Title(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            text = stringResource(R.string.main_settings)
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