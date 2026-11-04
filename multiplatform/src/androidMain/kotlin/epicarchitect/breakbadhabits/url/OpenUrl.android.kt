package epicarchitect.breakbadhabits.url

import android.content.Intent
import androidx.core.net.toUri
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import epicarchitect.breakbadhabits.infrastructure.Application

actual fun openUrl(url: String) {
    try {
        Application.startActivity(
            Intent(Intent.ACTION_VIEW, url.toUri())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    } catch (throwable: Throwable) {
        AnalyticsOperator.logError(throwable)
    }
}
