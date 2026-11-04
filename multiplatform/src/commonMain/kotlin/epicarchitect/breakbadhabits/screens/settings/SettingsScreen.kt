package epicarchitect.breakbadhabits.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import epicarchitect.breakbadhabits.export.BackupCache
import epicarchitect.breakbadhabits.export.BackupFailure
import epicarchitect.breakbadhabits.export.BackupResult
import epicarchitect.breakbadhabits.export.BackupTooLargeException
import epicarchitect.breakbadhabits.export.backupExportDateText
import epicarchitect.breakbadhabits.export.exportBackup
import epicarchitect.breakbadhabits.export.rememberBackupImportLauncher
import epicarchitect.breakbadhabits.export.restoreBackup
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.infrastructure.Monetization
import epicarchitect.breakbadhabits.monetization.PurchaseStore
import epicarchitect.breakbadhabits.monetization.isActive
import epicarchitect.breakbadhabits.monetization.rememberPremiumIsActive
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.cancel
import epicarchitect.breakbadhabits.resources.paywall_title
import epicarchitect.breakbadhabits.resources.settings_analytics_description
import epicarchitect.breakbadhabits.resources.settings_analytics_short
import epicarchitect.breakbadhabits.resources.settings_backup_create
import epicarchitect.breakbadhabits.resources.settings_backup_create_error
import epicarchitect.breakbadhabits.resources.settings_backup_invalid_file
import epicarchitect.breakbadhabits.resources.settings_backup_newer_version
import epicarchitect.breakbadhabits.resources.settings_backup_replace
import epicarchitect.breakbadhabits.resources.settings_backup_replace_description
import epicarchitect.breakbadhabits.resources.settings_backup_restore
import epicarchitect.breakbadhabits.resources.settings_backup_restore_dialog_title
import epicarchitect.breakbadhabits.resources.settings_backup_restore_error
import epicarchitect.breakbadhabits.resources.settings_backup_restore_exported_at
import epicarchitect.breakbadhabits.resources.settings_backup_restore_file
import epicarchitect.breakbadhabits.resources.settings_backup_restore_result_title
import epicarchitect.breakbadhabits.resources.settings_backup_restore_unknown_date
import epicarchitect.breakbadhabits.resources.settings_backup_restored
import epicarchitect.breakbadhabits.resources.settings_backup_restoring
import epicarchitect.breakbadhabits.resources.settings_backup_too_large_error
import epicarchitect.breakbadhabits.resources.settings_data_title
import epicarchitect.breakbadhabits.resources.settings_delete_all_data
import epicarchitect.breakbadhabits.resources.settings_delete_data_cancel
import epicarchitect.breakbadhabits.resources.settings_delete_data_confirm
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_message
import epicarchitect.breakbadhabits.resources.settings_delete_data_dialog_title
import epicarchitect.breakbadhabits.resources.settings_dialog_ok
import epicarchitect.breakbadhabits.resources.settings_premium_active_description
import epicarchitect.breakbadhabits.resources.settings_premium_unlock_description
import epicarchitect.breakbadhabits.resources.settings_privacy_policy
import epicarchitect.breakbadhabits.resources.settings_privacy_title
import epicarchitect.breakbadhabits.resources.settings_terms_of_use
import epicarchitect.breakbadhabits.resources.settings_title
import epicarchitect.breakbadhabits.screens.root.LocalNavBackStack
import epicarchitect.breakbadhabits.screens.root.RootRoute
import epicarchitect.breakbadhabits.screens.root.push
import epicarchitect.breakbadhabits.uikit.AdaptiveContent
import epicarchitect.breakbadhabits.uikit.AppBackButton
import epicarchitect.breakbadhabits.uikit.AppTopBar
import epicarchitect.breakbadhabits.uikit.Dialog
import epicarchitect.breakbadhabits.uikit.PreviewTheme
import epicarchitect.breakbadhabits.uikit.PrimaryButton
import epicarchitect.breakbadhabits.uikit.SecondaryButton
import epicarchitect.breakbadhabits.uikit.rememberAsyncAction
import epicarchitect.breakbadhabits.url.PRIVACY_POLICY_URL
import epicarchitect.breakbadhabits.url.TERMS_OF_USE_URL
import epicarchitect.breakbadhabits.url.openUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@Composable
fun SettingsScreen() {
    val navBackStack = LocalNavBackStack.current
    var analyticsEnabled by remember {
        mutableStateOf(AnalyticsOperator.isEnabled)
    }

    val isPremium = rememberPremiumIsActive()
    var showDeleteDataDialog by remember {
        mutableStateOf(false)
    }
    val deleteAllDataAction = rememberAsyncAction {
        val now = Clock.System.now()
        val currentPremiumStatus = Monetization.premiumStatus.value
            ?.takeIf { it.isActive(now) }
        withContext(Dispatchers.Default) {
            AppDatabase.clearAllTables()
            if (currentPremiumStatus != null) {
                AppDatabase.premiumStatusDao.save(currentPremiumStatus)
            }
        }
        AnalyticsOperator.isEnabled = false
        analyticsEnabled = false
        showDeleteDataDialog = false
    }

    var pendingBackup by remember {
        mutableStateOf(BackupCache.pending)
    }
    var restoreResult by remember {
        mutableStateOf<BackupResult?>(null)
    }
    var showBackupErrorDialog by remember {
        mutableStateOf(false)
    }
    var backupExportTooLarge by remember {
        mutableStateOf(false)
    }

    val exportAction = rememberAsyncAction(
        onError = { throwable ->
            AnalyticsOperator.logError(throwable)
            backupExportTooLarge = throwable is BackupTooLargeException
            showBackupErrorDialog = true
        }
    ) {
        exportBackup()
    }

    val restoreAction = rememberAsyncAction(
        onError = { throwable ->
            AnalyticsOperator.logError(throwable)
            BackupCache.pending = null
            pendingBackup = null
            restoreResult = BackupResult.Failed(BackupFailure.Error)
        }
    ) {
        val backup = pendingBackup
        if (backup != null) {
            val result = restoreBackup(backup.backup)
            BackupCache.pending = null
            pendingBackup = null
            restoreResult = result
        }
    }

    val backupImportLauncher = rememberBackupImportLauncher { result ->
        when (result) {
            is BackupResult.Ready -> {
                BackupCache.pending = result
                pendingBackup = result
            }

            is BackupResult.Failed -> {
                BackupCache.pending = null
                pendingBackup = null
                restoreResult = result
            }

            BackupResult.Restored -> Unit
        }
    }

    if (showDeleteDataDialog) {
        DeleteAllDataDialog(
            deleting = deleteAllDataAction.isRunning,
            onDismiss = {
                if (!deleteAllDataAction.isRunning) showDeleteDataDialog = false
            },
            onConfirm = deleteAllDataAction::invoke
        )
    }

    pendingBackup?.let { pending ->
        RestoreBackupDialog(
            fileName = pending.fileName,
            exportedAt = pending.backup.exportedAt,
            timeZone = pending.backup.timeZone,
            isRestoring = restoreAction.isRunning,
            onRestore = restoreAction::invoke,
            onDismiss = {
                if (!restoreAction.isRunning) {
                    BackupCache.pending = null
                    pendingBackup = null
                }
            }
        )
    }

    restoreResult?.let { result ->
        RestoreResultDialog(
            result = result,
            onDismiss = { restoreResult = null }
        )
    }

    if (showBackupErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showBackupErrorDialog = false
                backupExportTooLarge = false
            },
            title = {
                Text(text = stringResource(Res.string.settings_backup_create))
            },
            text = {
                Text(
                    text = stringResource(
                        if (backupExportTooLarge) {
                            Res.string.settings_backup_too_large_error
                        } else {
                            Res.string.settings_backup_create_error
                        }
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBackupErrorDialog = false
                        backupExportTooLarge = false
                    }
                ) {
                    Text(text = stringResource(Res.string.settings_dialog_ok))
                }
            }
        )
    }

    val navigationBarBottom = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val listState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            bottom = 24.dp + navigationBarBottom
        ),
        state = listState
    ) {
        item(key = "top_bar") {
            AppTopBar(
                title = stringResource(Res.string.settings_title),
                navigationIcon = {
                    AppBackButton(onClick = { navBackStack.removeLastOrNull() })
                }
            )
        }

        if (Monetization.purchasesAvailable || Monetization.store != PurchaseStore.Ios) {
            item(key = "premium") {
                AdaptiveContent {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                top = 8.dp,
                                end = 16.dp
                            )
                    ) {
                        ListItem(
                            modifier = Modifier.clickable(enabled = !isPremium) {
                                navBackStack.push(RootRoute.Paywall)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.WorkspacePremium,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.paywall_title))
                            },
                            supportingContent = {
                                Text(
                                    text = stringResource(
                                        if (isPremium) {
                                            Res.string.settings_premium_active_description
                                        } else {
                                            Res.string.settings_premium_unlock_description
                                        }
                                    )
                                )
                            },
                            trailingContent = if (isPremium) {
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
            }
        }

        item(key = "data_section") {
            AdaptiveContent {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SettingsSectionTitle(stringResource(Res.string.settings_data_title))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            modifier = Modifier.clickable { exportAction.invoke() },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.FileUpload,
                                    contentDescription = null
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.settings_backup_create))
                            }
                        )
                        SettingsDivider()
                        ListItem(
                            modifier = Modifier.clickable { backupImportLauncher.launch() },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.FileDownload,
                                    contentDescription = null
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.settings_backup_restore))
                            }
                        )
                        SettingsDivider()
                        ListItem(
                            modifier = Modifier.clickable { showDeleteDataDialog = true },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.DeleteForever,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            content = {
                                Text(
                                    text = stringResource(Res.string.settings_delete_all_data),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }
        }

        item(key = "privacy_section") {
            AdaptiveContent {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    SettingsSectionTitle(stringResource(Res.string.settings_privacy_title))
                    Card(modifier = Modifier.fillMaxWidth()) {
                        ListItem(
                            modifier = Modifier.clickable {
                                openUrl(PRIVACY_POLICY_URL)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.PrivacyTip,
                                    contentDescription = null
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.settings_privacy_policy))
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        SettingsDivider()
                        ListItem(
                            modifier = Modifier.clickable {
                                openUrl(TERMS_OF_USE_URL)
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Description,
                                    contentDescription = null
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.settings_terms_of_use))
                            },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        SettingsDivider()
                        ListItem(
                            modifier = Modifier.toggleable(
                                value = analyticsEnabled,
                                role = Role.Switch,
                                onValueChange = {
                                    analyticsEnabled = it
                                    AnalyticsOperator.isEnabled = it
                                }
                            ),
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Filled.Insights,
                                    contentDescription = null
                                )
                            },
                            content = {
                                Text(text = stringResource(Res.string.settings_analytics_short))
                            },
                            supportingContent = {
                                Text(text = stringResource(Res.string.settings_analytics_description))
                            },
                            trailingContent = {
                                Switch(
                                    checked = analyticsEnabled,
                                    onCheckedChange = null
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RestoreBackupDialog(
    fileName: String,
    exportedAt: Long,
    timeZone: String,
    isRestoring: Boolean,
    onRestore: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismiss = { if (!isRestoring) onDismiss() }) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(Res.string.settings_backup_restore_dialog_title),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(
                    Res.string.settings_backup_restore_file,
                    fileName.ifBlank { "—" }
                ),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(
                    Res.string.settings_backup_restore_exported_at,
                    backupExportDateText(exportedAt, timeZone)
                        ?: stringResource(Res.string.settings_backup_restore_unknown_date)
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.settings_backup_replace_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(20.dp))

            if (isRestoring) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.settings_backup_restoring),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.settings_backup_replace),
                    onClick = onRestore
                )

                Spacer(Modifier.height(8.dp))

                SecondaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.cancel),
                    onClick = onDismiss
                )
            }
        }
    }
}

@Composable
private fun RestoreResultDialog(
    result: BackupResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.settings_backup_restore_result_title))
        },
        text = {
            Text(
                text = when (result) {
                    BackupResult.Restored ->
                        stringResource(Res.string.settings_backup_restored)

                    is BackupResult.Failed -> when (result.failure) {
                        BackupFailure.InvalidFile ->
                            stringResource(Res.string.settings_backup_invalid_file)

                        BackupFailure.NewerVersion ->
                            stringResource(Res.string.settings_backup_newer_version)

                        BackupFailure.Error ->
                            stringResource(Res.string.settings_backup_restore_error)
                    }

                    is BackupResult.Ready ->
                        stringResource(Res.string.settings_backup_restore_error)
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.settings_dialog_ok))
            }
        }
    )
}

@Composable
private fun DeleteAllDataDialog(
    deleting: Boolean,
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
            TextButton(
                enabled = !deleting,
                onClick = onConfirm
            ) {
                Text(
                    text = stringResource(Res.string.settings_delete_data_confirm),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(
                enabled = !deleting,
                onClick = onDismiss
            ) {
                Text(text = stringResource(Res.string.settings_delete_data_cancel))
            }
        }
    )
}


@Composable
private fun SettingsSectionTitle(text: String) {
    Text(
        modifier = Modifier.padding(start = 4.dp, top = 24.dp, bottom = 8.dp),
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp),
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
    )
}

@Preview(widthDp = 360, heightDp = 440, name = "Settings rows")
@Composable
private fun SettingsRowsPreview() {
    PreviewTheme {
        Column(Modifier.padding(16.dp)) {
            SettingsSectionTitle("Данные")
            Card(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    modifier = Modifier.clickable {},
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.FileUpload,
                            contentDescription = null
                        )
                    },
                    content = { Text("Создать резервную копию") }
                )
                SettingsDivider()
                ListItem(
                    modifier = Modifier.clickable {},
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.FileDownload,
                            contentDescription = null
                        )
                    },
                    content = { Text("Восстановить из резервной копии") }
                )
                SettingsDivider()
                ListItem(
                    modifier = Modifier.toggleable(
                        value = true,
                        role = Role.Switch,
                        onValueChange = {}
                    ),
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.Insights,
                            contentDescription = null
                        )
                    },
                    content = { Text("Аналитика") },
                    supportingContent = { Text("Помогает улучшать приложение") },
                    trailingContent = {
                        Switch(checked = true, onCheckedChange = null)
                    }
                )
                SettingsDivider()
                ListItem(
                    modifier = Modifier.clickable {},
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    content = {
                        Text(
                            text = "Удалить все данные",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
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
            deleting = false,
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 480, name = "RestoreBackupDialog")
@Composable
private fun RestoreBackupDialogPreview() {
    PreviewTheme {
        RestoreBackupDialog(
            fileName = "breakbadhabits-backup-2026-11-26.json",
            exportedAt = 1764150000,
            timeZone = "Europe/Moscow",
            isRestoring = false,
            onRestore = {},
            onDismiss = {}
        )
    }
}

@Preview(widthDp = 360, heightDp = 240, name = "RestoreResultDialog")
@Composable
private fun RestoreResultDialogPreview() {
    PreviewTheme {
        RestoreResultDialog(
            result = BackupResult.Restored,
            onDismiss = {}
        )
    }
}
