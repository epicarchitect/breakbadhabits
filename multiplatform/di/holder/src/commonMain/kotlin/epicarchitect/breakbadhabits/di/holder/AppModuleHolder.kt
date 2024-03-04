package epicarchitect.breakbadhabits.di.holder

import epicarchitect.breakbadhabits.di.declaration.AppModule
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AppModuleHolder : AppModule {
    var current: AppModule? = null
    fun require() = checkNotNull(current)
    override val foundation get() = require().foundation
    override val logic get() = require().logic
//    override val presentation get() = require().presentation
    override val ui get() = require().ui
}