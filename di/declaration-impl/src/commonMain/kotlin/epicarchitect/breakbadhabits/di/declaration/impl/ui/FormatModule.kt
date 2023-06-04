package epicarchitect.breakbadhabits.di.declaration.impl.ui

import epicarchitect.breakbadhabits.di.declaration.ui.FormatModule

class FormatModuleImpl(
    external: FormatModuleExternal
) : FormatModuleExternal by external

interface FormatModuleExternal : FormatModule