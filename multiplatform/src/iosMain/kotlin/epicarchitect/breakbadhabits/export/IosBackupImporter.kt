package epicarchitect.breakbadhabits.export

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfFile
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypeJSON
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
internal class IosBackupImporter {
    private var delegate: DocumentPickerDelegate? = null

    fun importData(onResult: (BackupResult) -> Unit) {
        val rootViewController = topPresentingViewController()
        if (rootViewController == null) {
            onResult(BackupResult.Failed(BackupFailure.Error))
            return
        }

        val picker = UIDocumentPickerViewController(
            forOpeningContentTypes = listOf(UTTypeJSON),
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
    private val onResult: (BackupResult) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL
        val path = url?.path
        if (path == null) {
            onResult(BackupResult.Failed(BackupFailure.InvalidFile))
            return
        }

        val fileName = url.lastPathComponent ?: path.substringAfterLast('/')
        url.startAccessingSecurityScopedResource()
        val fileSize = runCatching { fileSize(path) }.getOrNull()
        if (fileSize == null || fileSize > MAX_BACKUP_BYTES.toLong()) {
            url.stopAccessingSecurityScopedResource()
            onResult(BackupResult.Failed(BackupFailure.InvalidFile))
            return
        }
        val data = runCatching { readData(path) }.getOrNull()
        url.stopAccessingSecurityScopedResource()

        if (data == null || data.size > MAX_BACKUP_BYTES) {
            onResult(BackupResult.Failed(BackupFailure.InvalidFile))
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            val result = runCatching { decodeBackup(data.decodeToString(), fileName) }
                .getOrElse { BackupResult.Failed(BackupFailure.Error) }
            dispatch_async(dispatch_get_main_queue()) {
                onResult(result)
            }
        }
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) = Unit

    @OptIn(ExperimentalForeignApi::class)
    private fun readData(path: String): ByteArray? {
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        val length = data.length.toInt()
        if (length == 0) return ByteArray(0)
        if (length > MAX_BACKUP_BYTES) return null

        val bytes = ByteArray(length)
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
        return bytes
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun fileSize(path: String): Long? {
    val attributes = NSFileManager.defaultManager.attributesOfItemAtPath(path, error = null)
    return (attributes?.get(NSFileSize) as? NSNumber)?.longLongValue
}
