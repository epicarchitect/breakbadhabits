package epicarchitect.breakbadhabits.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.export.ImportResult
import epicarchitect.breakbadhabits.export.exportHabitsCsv
import epicarchitect.breakbadhabits.export.rememberCsvImportLauncher
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.export_button_description
import epicarchitect.breakbadhabits.resources.paywall_title
import epicarchitect.breakbadhabits.resources.settings_analytics_description
import epicarchitect.breakbadhabits.resources.settings_analytics_short
import epicarchitect.breakbadhabits.resources.settings_data_title
import epicarchitect.breakbadhabits.resources.settings_delete_all_data
import epicarchitect.breakbadhabits.resources.settings_delete_data_cancel
import epicarchitect.breakbadhabits.resources.settings_delete_data_confirm
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_message
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_title
import epicarchitect.breakbadhabits.resources.settings_export_error
import epicarchitect.breakbadhabits.resources.settings_import_button
import epicarchitect.breakbadhabits.resources.settings_import_dialog_ok
import epicarchitect.breakbadhabits.resources.settings_import_dialog_title
import epicarchitect.breakbadhabits.resources.settings_import_error
import epicarchitect.breakbadhabits.resources.settings_import_success
import epicarchitect.breakbadhabits.resources.settings_premium_active_description
import epicarchitect.breakbadhabits.resources.settings_premium_unlock_description
import epicarchitect.breakbadhabits.resources.settings_privacy_policy
import epicarchitect.breakbadhabits.resources.settings_privacy_title
import epicarchitect.breakbadhabits.resources.settings_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppScaffold
import epicarchitect.breakbadhabits.uikit.rememberEnterAlwaysTopBarScrollBehavior
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.openUrl
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@Composable
fun SettingsScreen() {
    val navBackStack = LocalNavBackStack.current
    val coroutineScope = rememberCoroutineScope()
    var analyticsEnabled by remember {
        mutableStateOf(AnalyticsOperator.isEnabled)
    }

    val premiumCache by Monetization.premiumCache.collectAsState()
    val isPremium = premiumCache?.expiresAt?.let { it > Clock.System.now() } == true
    var showDeleteDataDialog by remember {
        mutableStateOf(false)
    }
    var importResult by remember {
        mutableStateOf<ImportResult?>(null)
    }
    val importLauncher = rememberCsvImportLauncher { result ->
        importResult = result
    }

    if (showDeleteDataDialog) {
        DeleteAllDataDialog(
            onDismiss = { showDeleteDataDialog = false },
            onConfirm = {
                coroutineScope.launch {
                    AppDatabase.habitsDao.deleteAll()
                    AppDatabase.habitWidgetDao.deleteAll()
                    showDeleteDataDialog = false
                }
            }
        )
    }

    importResult?.let { result ->
        AlertDialog(
            onDismissRequest = { importResult = null },
            title = {
                Text(text = stringResource(Res.string.settings_import_dialog_title))
            },
            text = {
                Text(
                    text = if (result is ImportResult.Success) {
                        stringResource(Res.string.settings_import_success)
                    } else {
                        stringResource(Res.string.settings_import_error)
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = { importResult = null }) {
                    Text(text = stringResource(Res.string.settings_import_dialog_ok))
                }
            }
        )
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val exportErrorMessage = stringResource(Res.string.settings_export_error)
    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()
    val topBarScrollBehavior = rememberEnterAlwaysTopBarScrollBehavior(listState)

    AppScaffold(
        title = stringResource(Res.string.settings_title),
        topBarScrollBehavior = topBarScrollBehavior,
        navigationIcon = {
            AppBackButton(onClick = { navBackStack.removeLastOrNull() })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { topBarHeight ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = topBarHeight + 8.dp,
                end = 16.dp,
                bottom = 24.dp + navigationBarBottom
            ),
            state = listState
        ) {
                item(key = "premium") {
                    SettingsGroup(
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                        )
                    ) {
                        SettingsRow(
                            icon = Icons.Filled.WorkspacePremium,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = stringResource(Res.string.paywall_title),
                            subtitle = stringResource(
                                if (isPremium) {
                                    Res.string.settings_premium_active_description
                                } else {
                                    Res.string.settings_premium_unlock_description
                                }
                            ),
                            onClick = if (isPremium) {
                                null
                            } else {
                                {
                                    navBackStack.push(RootRoute.Paywall(source = "settings"))
                                }
                            },
                            trailing = if (isPremium) {
                                null
                            } else {
                                {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        )
                    }
                }

                item(key = "data_section") {
                    SettingsSectionTitle(stringResource(Res.string.settings_data_title))
                    SettingsGroup {
                        SettingsRow(
                            icon = Icons.Filled.FileDownload,
                            title = stringResource(Res.string.export_button_description),
                            onClick = {
                                coroutineScope.launch {
                                    if (runCatching { exportHabitsCsv() }.isFailure) {
                                        snackbarHostState.showSnackbar(exportErrorMessage)
                                    }
                                }
                            }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Filled.FileUpload,
                            title = stringResource(Res.string.settings_import_button),
                            onClick = {
                                importLauncher.launch()
                            }
                        )
                        SettingsDivider()
                        SettingsRow(
                            icon = Icons.Filled.DeleteForever,
                            iconTint = MaterialTheme.colorScheme.error,
                            title = stringResource(Res.string.settings_delete_all_data),
                            titleColor = MaterialTheme.colorScheme.error,
                            onClick = { showDeleteDataDialog = true }
                        )
                    }
                }

                item(key = "privacy_section") {
                    SettingsSectionTitle(stringResource(Res.string.settings_privacy_title))
                    SettingsGroup {
                        SettingsRow(
                            icon = Icons.Filled.PrivacyTip,
                            title = stringResource(Res.string.settings_privacy_policy),
                            onClick = {
                                openUrl(PRIVACY_POLICY_URL)
                            },
                            trailing = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        SettingsDivider()
                        SettingsSwitchRow(
                            icon = Icons.Filled.Insights,
                            title = stringResource(Res.string.settings_analytics_short),
                            subtitle = stringResource(Res.string.settings_analytics_description),
                            checked = analyticsEnabled,
                            onCheckedChange = {
                                analyticsEnabled = it
                                AnalyticsOperator.isEnabled = it
                            }
                        )
                    }
                }
            }
        }
    }

@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(
            start = 4.dp,
            top = 22.dp,
            bottom = 8.dp
        ),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SettingsGroup(
    modifier: Modifier = Modifier,
    border: BorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
    ),
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = border
    ) {
        Column(content = { content() })
    }
}

@Composable
private fun SettingsRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = modifier
        .fillMaxWidth()
        .then(
            if (onClick != null) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            }
        )

    Row(
        modifier = rowModifier.padding(
            horizontal = 16.dp,
            vertical = 14.dp
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = iconTint
            )
            Spacer(Modifier.width(14.dp))
        }

        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = titleColor
            )
            if (subtitle != null) {
                Spacer(Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (trailing != null) {
            Spacer(Modifier.width(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                trailing()
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(14.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = null
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 52.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    )
}

@Composable
private fun DeleteAllDataDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.settings_delete_data_dialog_title))
        },
        text = {
            Text(text = stringResource(Res.string.settings_delete_data_dialog_message))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(Res.string.settings_delete_data_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.settings_delete_data_cancel))
            }
        }
    )
}


@Preview(widthDp = 360, heightDp = 360, name = "Settings rows")
@Composable
private fun SettingsRowsPreview() {
    PreviewTheme {
        Column(Modifier.padding(16.dp)) {
            SettingsSectionTitle("Данные")
            SettingsGroup {
                SettingsRow(
                    icon = Icons.Filled.FileDownload,
                    title = "Экспорт в CSV",
                    subtitle = "Сохранить все записи",
                    onClick = {}
                )
                SettingsDivider()
                SettingsSwitchRow(
                    icon = Icons.Filled.Insights,
                    title = "Аналитика",
                    subtitle = "Помогает улучшать приложение",
                    checked = true,
                    onCheckedChange = {}
                )
                SettingsDivider()
                SettingsRow(
                    icon = Icons.Filled.DeleteForever,
                    iconTint = MaterialTheme.colorScheme.error,
                    title = "Удалить все данные",
                    titleColor = MaterialTheme.colorScheme.error,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(widthDp = 360, heightDp = 300, name = "DeleteAllDataDialog")
@Composable
private fun DeleteAllDataDialogPreview() {
    PreviewTheme {
        DeleteAllDataDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
