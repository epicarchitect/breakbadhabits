package epicarchitect.architecture

import kotlin.reflect.KClass

abstract class EpicArchitecture(
    private val outputs: Iterable<Output>,
    private val inputs: Iterable<Input>,
    private val outputConfigs: Iterable<OutputConfig>,
    private val inputConfigs: Iterable<InputConfig>
) {

    fun getOutputConfig(
        keyClass: KClass<*>,
        valueClass: KClass<*>
    ) = outputConfigs.first {
        it.keyClass == keyClass && it.valueClass == valueClass
    }

    fun getInputConfig(valueClass: KClass<*>) = inputConfigs.first {
        it.valueClass == valueClass
    }

    fun getOutput(outputClass: KClass<out Output>) = outputs.first {
        it::class == outputClass
    }

    fun getInput(inputClass: KClass<out Input>) = inputs.first {
        it::class == inputClass
    }

    data class OutputConfig(
        val keyClass: KClass<*>,
        val valueClass: KClass<*>,
        val outputClass: KClass<out Output>
    )

    data class InputConfig(
        val valueClass: KClass<*>,
        val inputClass: KClass<out Input>
    )

    interface Output

    interface Input

}