package epicarchitect.breakbadhabits.di.declaration.impl.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule

class FoundationModuleImpl(
    external: FoundationModuleExternal
) : FoundationModule, FoundationModuleExternal by external {
    override val coroutines by lazy {
        CoroutinesModuleImpl()
    }

    override val identification by lazy {
        IdentificationModuleImpl(
            external = identificationModuleExternal
        )
    }
}

interface FoundationModuleExternal {
    val identificationModuleExternal: IdentificationModuleExternal
}
