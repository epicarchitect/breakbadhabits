package epicarchitect.breakbadhabits.di.declaration.impl.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.IdentificationModule

class IdentificationModuleImpl(
    external: IdentificationModuleExternal
) : IdentificationModuleExternal by external

interface IdentificationModuleExternal : IdentificationModule
