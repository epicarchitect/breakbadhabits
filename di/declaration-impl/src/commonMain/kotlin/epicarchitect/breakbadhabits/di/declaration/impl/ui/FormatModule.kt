package epicarchitect.breakbadhabits.di.declaration.impl.ui

import epicarchitect.breakbadhabits.di.declaration.ui.FormatModule
import epicarchitect.breakbadhabits.ui.format.DateTimeFormatter
import epicarchitect.breakbadhabits.ui.format.DurationFormatter

class FormatModuleImpl(
    external: FormatModuleExternal
) : FormatModuleExternal by external

interface FormatModuleExternal : FormatModule