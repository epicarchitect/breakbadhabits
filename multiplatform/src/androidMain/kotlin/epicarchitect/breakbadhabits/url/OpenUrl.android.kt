package epicarchitect.breakbadhabits.url

import android.content.Intent
import android.net.Uri
import epicarchitect.breakbadhabits.infrastructure.Application
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator
import androidx.core.net.toUri

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
