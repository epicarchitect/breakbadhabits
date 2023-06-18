package epicarchitect.breakbadhabits.di.declaration.main.ui

import epicarchitect.breakbadhabits.di.declaration.ui.FormatModule

class FormatModule(
    externals: FormatModuleExternals
) : FormatModuleExternals by externals

interface FormatModuleExternals : FormatModule
