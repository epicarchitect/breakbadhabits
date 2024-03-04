package epicarchitect.breakbadhabits.di.declaration.main.ui

import epicarchitect.breakbadhabits.di.declaration.ui.UiModule

class UiModule(
    externals: UiModuleExternals
) : UiModule, UiModuleExternals by externals {
    override val format by lazy {
        FormatModule(formatModuleExternal)
    }
}

interface UiModuleExternals {
    val formatModuleExternal: FormatModuleExternals
}