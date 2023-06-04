package epicarchitect.breakbadhabits.ui.settings

import androidx.compose.runtime.Composable

@Composable
fun AppSettings(
    openWidgetSettings: () -> Unit
) {
//    val darkModeManager = LocalDarkModeManager.current
//    val darkMode by darkModeManager.mode
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ) {
//        Text(
//            modifier = Modifier
//                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
//            text = stringResource(R.string.main_settings),
//            type = Text.Type.Title,
//            priority = Text.Priority.High
//        )
//
//        Text(
//            modifier = Modifier
//                .padding(
//                    start = 16.dp,
//                    top = 8.dp,
//                    end = 8.dp,
//                    bottom = 4.dp
//                ),
//            text = stringResource(R.string.appSettings_themeSelection_description),
//        )
//
//        RadioButton(
//            text = stringResource(R.string.appSettings_themeSelection_systemTheme),
//            selected = darkMode == DarkMode.BY_SYSTEM,
//            onSelect = {
//                darkModeManager.changeMode(DarkMode.BY_SYSTEM)
//            }
//        )
//
//        RadioButton(
//            text = stringResource(R.string.appSettings_themeSelection_lightTheme),
//            selected = darkMode == DarkMode.DISABLED,
//            onSelect = {
//                darkModeManager.changeMode(DarkMode.DISABLED)
//            }
//        )
//
//        RadioButton(
//            text = stringResource(R.string.appSettings_themeSelection_darkTheme),
//            selected = darkMode == DarkMode.ENABLED,
//            onSelect = {
//                darkModeManager.changeMode(DarkMode.ENABLED)
//            }
//        )
//
//        Text(
//            modifier = Modifier.padding(
//                start = 16.dp,
//                top = 8.dp,
//                end = 8.dp,
//                bottom = 4.dp
//            ),
//            text = stringResource(R.string.appSettings_widgets_description)
//        )
//
//        Button(
//            modifier = Modifier.padding(
//                start = 16.dp,
//                top = 8.dp,
//                end = 8.dp,
//                bottom = 16.dp
//            ),
//            onClick = openWidgetSettings,
//            text = stringResource(R.string.appSettings_widgets_button)
//        )
//    }
}