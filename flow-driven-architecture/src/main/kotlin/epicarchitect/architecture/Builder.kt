package epicarchitect.architecture

import kotlinx.coroutines.flow.Flow

class FlowArchitectureBuilder {
    val outputs = mutableListOf<FlowDrivenArchitecture.Output<*, *>>()
    val inputs = mutableListOf<FlowDrivenArchitecture.Input<*>>()
    val outputConfigs = mutableListOf<EpicArchitecture.OutputConfig>()
    val inputConfigs = mutableListOf<EpicArchitecture.InputConfig>()

    inline fun <reified KEY : Any?, reified VALUE : Any?> output(crossinline process: (KEY) -> Flow<VALUE>) {
        val output = FlowDrivenArchitecture.Output<KEY, VALUE> { process(it) }
        outputs.add(output)
        outputConfigs.add(
            EpicArchitecture.OutputConfig(
                KEY::class,
                VALUE::class,
                output::class
            )
        )
    }


    inline fun <reified VALUE : Any?> input(crossinline process: (VALUE) -> Unit) {
        val input = FlowDrivenArchitecture.Input<VALUE> { process(it) }
        inputs.add(input)
        inputConfigs.add(
            EpicArchitecture.InputConfig(
                VALUE::class,
                input::class
            )
        )
    }

    fun build() = FlowDrivenArchitecture(
        outputs,
        inputs,
        outputConfigs,
        inputConfigs
    )
}

fun FlowDrivenArchitecture(setup: FlowArchitectureBuilder.() -> Unit) = FlowArchitectureBuilder().apply(setup).build()
