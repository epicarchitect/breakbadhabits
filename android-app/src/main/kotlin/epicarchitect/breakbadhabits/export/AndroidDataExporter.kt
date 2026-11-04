package epicarchitect.breakbadhabits.export

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AndroidDataExporter(
    private val context: Context
) : DataExporter {

    override suspend fun exportCsv(
        fileName: String,
        content: ByteArray
    ) {
        val file = withContext(Dispatchers.IO) {
            File(exportDirectory(), fileName).apply {
                writeBytes(content)
            }
        }
        share(file)
    }

    private fun exportDirectory() = File(context.cacheDir, "exports").apply {
        mkdirs()
    }

    private fun share(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(intent, null).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
