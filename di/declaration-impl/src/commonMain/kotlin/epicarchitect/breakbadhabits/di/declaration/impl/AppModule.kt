package epicarchitect.breakbadhabits.di.declaration.impl

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.di.declaration.ui.UiModule

class AppModuleImpl(override val ui: UiModule) : AppModule {
    override val presentation = ui.presentation
    override val logic = presentation.logic
    override val foundation = logic.foundation
}