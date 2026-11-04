package epicarchitect.breakbadhabits.export

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfFile
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeCommaSeparatedText
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
class IosDataImporter {
    private var delegate: DocumentPickerDelegate? = null

    fun importData(onResult: (ImportResult) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        if (rootViewController == null) {
            onResult(ImportResult.Error)
            return
        }

        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = listOf(UTTypeCommaSeparatedText),
            asCopy = true
        )
        val pickerDelegate = DocumentPickerDelegate(onResult)
        delegate = pickerDelegate
        picker.delegate = pickerDelegate
        rootViewController.presentViewController(picker, animated = true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private class DocumentPickerDelegate(
    private val onResult: (ImportResult) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
        val path = url?.path
        if (path == null) {
            onResult(ImportResult.InvalidFile)
            return
        }

        url.startAccessingSecurityScopedResource()
        val csv = runCatching { readText(path) }.getOrNull()
        url.stopAccessingSecurityScopedResource()

        if (csv == null) {
            onResult(ImportResult.InvalidFile)
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            val result = runCatching { importHabitsCsv(csv) }
                .getOrDefault(ImportResult.Error)
            dispatch_async(dispatch_get_main_queue()) {
                onResult(result)
            }
        }
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) = Unit

    @OptIn(ExperimentalForeignApi::class)
    private fun readText(path: String): String? {
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        val length = data.length.toInt()
        if (length == 0) return ""

        val bytes = ByteArray(length)
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
        return bytes.decodeToString()
    }
}
