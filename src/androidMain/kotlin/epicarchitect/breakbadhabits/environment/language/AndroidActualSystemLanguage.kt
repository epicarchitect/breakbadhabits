package epicarchitect.breakbadhabits.environment.language

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

actual object ActualSystemLanguage {
    private val mutableState = MutableStateFlow(resolve(Locale.getDefault()))
    actual val state: StateFlow<AppLanguage> = mutableState

    fun update(locale: Locale) {
        mutableState.value = resolve(locale)
    }

    private fun resolve(locale: Locale) = when (locale.language) {
        "ru" -> AppLanguage.RUSSIAN
        else -> AppLanguage.ENGLISH
    }
}