package breakbadhabits.android.app.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import breakbadhabits.android.app.App
import breakbadhabits.android.app.R
import breakbadhabits.android.app.utils.NightModeManager
import breakbadhabits.android.compose.molecule.Button
import breakbadhabits.android.compose.molecule.RadioButton
import breakbadhabits.android.compose.molecule.Text
import breakbadhabits.android.compose.molecule.Title

@Composable
fun AppSettingsScreen(openWidgetSettings: () -> Unit) {
    var mode by remember { mutableStateOf(App.architecture.nightModeManager.mode) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(WindowInsets.systemBars.asPaddingValues())
                .fillMaxSize()
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
                selected = mode == NightModeManager.Mode.FOLLOW_SYSTEM,
                onSelect = {
                    mode = NightModeManager.Mode.FOLLOW_SYSTEM
                    App.architecture.nightModeManager.mode = mode
                }
            )

            RadioButton(
                text = stringResource(R.string.appSettings_themeSelection_lightTheme),
                selected = mode == NightModeManager.Mode.NOT_NIGHT,
                onSelect = {
                    mode = NightModeManager.Mode.NOT_NIGHT
                    App.architecture.nightModeManager.mode = mode
                }
            )

            RadioButton(
                text = stringResource(R.string.appSettings_themeSelection_darkTheme),
                selected = mode == NightModeManager.Mode.NIGHT,
                onSelect = {
                    mode = NightModeManager.Mode.NIGHT
                    App.architecture.nightModeManager.mode = mode
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
                onClick = {
                    openWidgetSettings()
                },
                text = stringResource(R.string.appSettings_widgets_button)
            )
        }
    }
}