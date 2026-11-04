package epicarchitect.breakbadhabits.export

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
internal actual fun rememberBackupImportLauncher(
    onResult: (BackupResult) -> Unit
): BackupImportLauncher {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val resultCallback = rememberUpdatedState(onResult)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        coroutineScope.launch {
            val result = runCatching {
                withContext(Dispatchers.IO) {
                    val bytes = context.contentResolver
                        .openInputStream(uri)
                        ?.use { readLimited(it, MAX_BACKUP_BYTES) }
                        ?: return@withContext BackupResult.Failed(BackupFailure.InvalidFile)

                    decodeBackup(
                        text = bytes.decodeToString(),
                        fileName = displayName(context, uri)
                    )
                }
            }.getOrElse {
                BackupResult.Failed(BackupFailure.Error)
            }
            resultCallback.value(result)
        }
    }

    return remember(launcher) {
        BackupImportLauncher {
            launcher.launch(arrayOf("*/*"))
        }
    }
}

private fun displayName(
    context: android.content.Context,
    uri: android.net.Uri
): String {
    val name = runCatching {
        context.contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }.getOrNull()

    return name
        ?: uri.lastPathSegment?.substringAfterLast('/')
        ?: ""
}

private fun readLimited(input: InputStream, limit: Int): ByteArray? {
    val output = ByteArrayOutputStream(minOf(limit, 8192))
    val buffer = ByteArray(8192)
    var total = 0

    while (true) {
        val read = input.read(buffer)
        if (read == -1) break
        total += read
        if (total > limit) return null
        output.write(buffer, 0, read)
    }

    return output.toByteArray()
}
