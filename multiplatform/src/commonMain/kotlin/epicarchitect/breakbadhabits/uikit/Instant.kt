package epicarchitect.breakbadhabits.uikit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

val LocalInstant = staticCompositionLocalOf<State<Instant>> {
    error("InstantProvider is not present")
}

@Composable
fun InstantProvider(content: @Composable () -> Unit) {
    val currentInstant = remember { mutableStateOf(Clock.System.now()) }

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1.seconds)
            currentInstant.value = Clock.System.now()
        }
    }

    CompositionLocalProvider(
        LocalInstant provides currentInstant,
        content = content
    )
}
