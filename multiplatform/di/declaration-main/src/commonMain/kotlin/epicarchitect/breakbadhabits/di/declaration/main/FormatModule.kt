package epicarchitect.breakbadhabits.di.declaration.main

import epicarchitect.breakbadhabits.di.declaration.FormatModule

class FormatModule(
    externals: FormatModuleExternals
) : FormatModule by externals

interface FormatModuleExternals : FormatModule