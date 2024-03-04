package epicarchitect.breakbadhabits.di.declaration.main

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.di.declaration.main.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.ui.UiModule

class AppModule(
    override val ui: UiModule,
    override val logic: LogicModule
) : AppModule {
    //    override val presentation = ui.presentation
//    override val logic = ui.logic
    override val foundation = logic.foundation
}