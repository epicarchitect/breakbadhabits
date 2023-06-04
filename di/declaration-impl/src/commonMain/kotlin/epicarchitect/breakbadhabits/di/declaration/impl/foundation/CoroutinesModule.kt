package epicarchitect.breakbadhabits.di.declaration.impl.foundation

import epicarchitect.breakbadhabits.di.declaration.foundation.CoroutinesModule

class CoroutinesModuleImpl(
    external: CoroutinesModuleExternal
) : CoroutinesModuleExternal by external

interface CoroutinesModuleExternal : CoroutinesModule