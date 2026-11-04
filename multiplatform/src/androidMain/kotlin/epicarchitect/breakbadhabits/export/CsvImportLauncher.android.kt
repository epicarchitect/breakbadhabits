package epicarchitect.breakbadhabits.export

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

@Composable
actual fun rememberCsvImportLauncher(
    onResult: (ImportResult) -> Unit
): CsvImportLauncher {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val resultCallback = rememberUpdatedState(onResult)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult

        coroutineScope.launch {
            val result = runCatching {
                val csv = withContext(Dispatchers.IO) {
                    context.contentResolver
                        .openInputStream(uri)
                        ?.bufferedReader()
                        ?.use { it.readText() }
                } ?: return@runCatching ImportResult.InvalidFile
                importHabitsCsv(csv)
            }.getOrDefault(ImportResult.Error)
            resultCallback.value(result)
        }
    }

    return remember(launcher) {
        CsvImportLauncher {
            launcher.launch(arrayOf("text/*", "application/csv"))
        }
    }
}
