@file:Suppress("UNCHECKED_CAST")

package epicarchitect.architecture

import kotlinx.coroutines.flow.Flow

class FlowDrivenArchitecture(
    outputs: Iterable<Output<*, *>>,
    inputs: Iterable<Input<*>>,
    outputConfigs: Iterable<OutputConfig>,
    inputConfigs: Iterable<InputConfig>
) : EpicArchitecture(
    outputs,
    inputs,
    outputConfigs,
    inputConfigs
) {
    fun interface Output<KEY, VALUE : Any?> : EpicArchitecture.Output, (KEY) -> Flow<VALUE>

    fun interface Input<VALUE : Any?> : EpicArchitecture.Input, (VALUE) -> Unit

    inline fun <reified KEY, reified VALUE> output(key: KEY): Flow<VALUE> {
        val config = getOutputConfig(KEY::class, VALUE::class)
        val output = getOutput(config.outputClass) as Output<KEY, VALUE>
        return output(key)
    }

    inline fun <reified VALUE> input(value: VALUE) {
        val config = getInputConfig(VALUE::class)
        val input = getInput(config.inputClass) as Input<VALUE>
        input(value)
    }
}

