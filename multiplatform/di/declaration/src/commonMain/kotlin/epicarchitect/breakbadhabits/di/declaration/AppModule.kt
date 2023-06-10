package epicarchitect.breakbadhabits.di.declaration

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule
import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.PresentationModule
import epicarchitect.breakbadhabits.di.declaration.ui.UiModule

interface AppModule {
    val ui: UiModule
    val presentation: PresentationModule
    val logic: LogicModule
    val foundation: FoundationModule
}