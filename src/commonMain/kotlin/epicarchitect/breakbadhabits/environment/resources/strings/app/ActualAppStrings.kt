package epicarchitect.breakbadhabits.environment.resources.strings.app

import epicarchitect.breakbadhabits.environment.language.ActualAppLanguage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ActualAppStrings(
    coroutineScope: CoroutineScope,
    actualAppLanguage: ActualAppLanguage
) {
    val state = actualAppLanguage.state.map(::LocalizedAppStrings).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LocalizedAppStrings(actualAppLanguage.state.value)
    )
}