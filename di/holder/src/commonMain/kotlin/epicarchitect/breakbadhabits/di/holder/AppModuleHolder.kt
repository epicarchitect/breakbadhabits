package epicarchitect.breakbadhabits.di.holder

import epicarchitect.breakbadhabits.di.declaration.AppModule

object AppModuleHolder : AppModule {
    lateinit var current: AppModule
    override val foundation get() = current.foundation
    override val logic get() = current.logic
    override val presentation get() = current.presentation
    override val ui get() = current.ui
}