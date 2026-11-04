package epicarchitect.breakbadhabits.monetization

import android.content.Intent
import ru.rustore.sdk.pay.RuStorePayClient
import ru.rustore.sdk.pay.model.SdkTheme

object PurchasePlatform {
    fun create(): MonetizationEnvironment = RuStoreMonetizationEnvironment()

    fun handlePayIntent(intent: Intent?) {
        intent ?: return
        runCatching {
            RuStorePayClient.instance
                .getIntentInteractor()
                .proceedIntent(intent, SdkTheme.DARK)
        }
    }
}
