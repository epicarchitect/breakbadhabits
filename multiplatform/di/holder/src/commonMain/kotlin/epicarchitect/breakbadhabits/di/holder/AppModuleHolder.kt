package epicarchitect.breakbadhabits.di.holder

import epicarchitect.breakbadhabits.di.declaration.AppModule

//@ThreadLocal
object AppModuleHolder {
    var current: AppModule? = null
    fun require() = checkNotNull(current)
}