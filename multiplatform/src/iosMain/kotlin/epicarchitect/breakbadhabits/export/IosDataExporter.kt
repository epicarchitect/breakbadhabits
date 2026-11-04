package epicarchitect.breakbadhabits.export

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileProtectionComplete
import platform.Foundation.NSFileProtectionKey
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.popoverPresentationController

class IosDataExporter : DataExporter {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun exportBackup(
        fileName: String,
        content: ByteArray
    ) {
        val url = NSURL.fileURLWithPath(NSTemporaryDirectory() + fileName)
        val data = content.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = content.size.toULong()
            )
        }
        data.writeToURL(url, atomically = true)
        url.path?.let(::protectFile)
        share(url)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun protectFile(path: String) {
        NSFileManager.defaultManager.setAttributes(
            attributes = mapOf(
                NSFileProtectionKey to NSFileProtectionComplete
            ),
            ofItemAtPath = path,
            error = null
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun share(url: NSURL) {
        val presenter = topPresentingViewController()
        if (presenter == null) {
            url.path?.let { path ->
                NSFileManager.defaultManager.removeItemAtPath(path, error = null)
            }
            return
        }

        val controller = UIActivityViewController(
            activityItems = listOf(url),
            applicationActivities = null
        )
        controller.completionWithItemsHandler = { _, _, _, _ ->
            url.path?.let { path ->
                NSFileManager.defaultManager.removeItemAtPath(path, error = null)
            }
        }
        controller.popoverPresentationController?.let { popover ->
            val sourceView = presenter.view
            popover.sourceView = sourceView
            popover.sourceRect = sourceView.bounds
        }
        presenter.presentViewController(
            viewControllerToPresent = controller,
            animated = true,
            completion = null
        )
    }
}
