package epicarchitect.breakbadhabits.android.app.coroutines

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

class DefaultCoroutineDispatchers : CoroutineDispatchers {
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
    override val main = Dispatchers.Main
}