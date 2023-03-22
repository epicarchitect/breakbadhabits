package breakbadhabits.app.di

import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers

object DefaultCoroutineDispatchers : CoroutineDispatchers {
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
    override val main = Dispatchers.Main
}