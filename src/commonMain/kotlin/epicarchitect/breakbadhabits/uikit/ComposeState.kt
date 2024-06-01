package epicarchitect.breakbadhabits.uikit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.sqldelight.Query
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfList
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import kotlinx.coroutines.flow.map

sealed class FlowState<out T> {
    object AwaitFirstValue : FlowState<Nothing>()
    data class LastValue<T>(val value: T) : FlowState<T>()
}

@Composable
fun <T : Any> stateOfOneOrNull(
    vararg keys: Any?,
    query: () -> Query<T>
) = remember(keys) {
    query().flowOfOneOrNull().map {
        FlowState.LastValue(it)
    }
}.collectAsState(FlowState.AwaitFirstValue)

@Composable
fun <T : Any> stateOfList(
    vararg keys: Any?,
    query: () -> Query<T>
) = remember(keys) {
    query().flowOfList().map {
        FlowState.LastValue(it)
    }
}.collectAsState(FlowState.AwaitFirstValue)

@Composable
fun FlowStateContainer(
    states: List<State<FlowState<*>>>,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit,
    content: @Composable (List<*>) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        transitionSpec = { fadeIn().togetherWith(fadeOut()) },
        targetState = states.all { it.value is FlowState.LastValue }
    ) { loaded ->
        if (loaded) {
            content(states.map { (it.value as FlowState.LastValue).value })
        } else {
            emptyContent()
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T> FlowStateContainer(
    state: State<FlowState<T>>,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = { Box(modifier = Modifier.fillMaxSize()) },
    content: @Composable (T) -> Unit,
) {
    FlowStateContainer(
        modifier = modifier,
        states = listOf(state),
        emptyContent = emptyContent,
        content = {
            content(it[0] as T)
        }
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2> FlowStateContainer(
    state1: State<FlowState<T1>>,
    state2: State<FlowState<T2>>,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = { Box(modifier = Modifier.fillMaxSize()) },
    content: @Composable (T1, T2) -> Unit,
) {
    FlowStateContainer(
        modifier = modifier,
        states = listOf(state1, state2),
        emptyContent = emptyContent,
        content = {
            content(
                it[0] as T1,
                it[1] as T2
            )
        }
    )
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T1, T2, T3> FlowStateContainer(
    state1: State<FlowState<T1>>,
    state2: State<FlowState<T2>>,
    state3: State<FlowState<T3>>,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit = { Box(modifier = Modifier.fillMaxSize()) },
    content: @Composable (T1, T2, T3) -> Unit,
) {
    FlowStateContainer(
        modifier = modifier,
        states = listOf(state1, state2, state3),
        emptyContent = emptyContent,
        content = {
            content(
                it[0] as T1,
                it[1] as T2,
                it[2] as T3,
            )
        }
    )
}