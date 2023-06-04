package epicarchitect.breakbadhabits.di.holder

import epicarchitect.breakbadhabits.di.declaration.AppModule
import kotlin.properties.Delegates

object AppModuleHolder : AppModule {
    var current: AppModule by Delegates.notNull()
    override val foundation get() = current.foundation
    override val logic get() = current.logic
    override val presentation get() = current.presentation
    override val ui get() = current.ui
}