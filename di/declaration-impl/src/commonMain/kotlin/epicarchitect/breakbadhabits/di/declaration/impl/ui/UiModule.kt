package epicarchitect.breakbadhabits.di.declaration.impl.ui

import epicarchitect.breakbadhabits.di.declaration.presentation.PresentationModule
import epicarchitect.breakbadhabits.di.declaration.ui.UiModule

class UiModuleImpl(
    override val presentation: PresentationModule,
    external: UiModuleExternal
) : UiModule, UiModuleExternal by external {
    override val format by lazy {
        FormatModuleImpl(formatModuleExternal)
    }
}

interface UiModuleExternal {
    val formatModuleExternal: FormatModuleExternal
}