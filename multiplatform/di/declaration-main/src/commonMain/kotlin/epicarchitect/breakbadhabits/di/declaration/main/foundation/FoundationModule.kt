package epicarchitect.breakbadhabits.di.declaration.main.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule

class FoundationModule : FoundationModule {
    override val coroutines by lazy(::CoroutinesModule)
    override val identification by lazy(::IdentificationModule)
}