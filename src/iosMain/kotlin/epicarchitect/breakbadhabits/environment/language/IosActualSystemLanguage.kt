package epicarchitect.breakbadhabits.environment.language

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object ActualSystemLanguage {
    // TODO
    actual val state: StateFlow<AppLanguage> = MutableStateFlow(AppLanguage.ENGLISH)
}