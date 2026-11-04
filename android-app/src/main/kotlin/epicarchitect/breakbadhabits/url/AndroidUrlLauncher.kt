package epicarchitect.breakbadhabits.url

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import epicarchitect.breakbadhabits.infrastructure.AnalyticsOperator

class AndroidUrlLauncher(
    private val context: Context
) : UrlLauncher {
    override fun openUrl(url: String) {
        try {
            context.applicationContext.startActivity(
                Intent(Intent.ACTION_VIEW, url.toUri())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } catch (throwable: Throwable) {
            AnalyticsOperator.logError(throwable)
        }
    }
}
