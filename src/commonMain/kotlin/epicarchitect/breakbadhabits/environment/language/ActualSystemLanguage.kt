package epicarchitect.breakbadhabits.environment.language

import kotlinx.coroutines.flow.StateFlow

expect object ActualSystemLanguage {
    val state: StateFlow<AppLanguage>
}