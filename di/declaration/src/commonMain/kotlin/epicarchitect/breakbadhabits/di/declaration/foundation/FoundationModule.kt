package epicarchitect.breakbadhabits.di.declaration.foundation

interface FoundationModule {
    val coroutines: CoroutinesModule
    val identification: IdentificationModule
}