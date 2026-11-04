package epicarchitect.breakbadhabits.url

import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

class IosUrlLauncher : UrlLauncher {
    @OptIn(ExperimentalForeignApi::class)
    override fun openUrl(url: String) {
        val nsUrl = NSURL.URLWithString(url)
        val opened = nsUrl != null && UIApplication.sharedApplication.openURL(nsUrl)
        if (opened.not()) {
            AnalyticsOperator.logError(IllegalStateException("Failed to open url: $url"))
        }
    }
}
